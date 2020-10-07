package apps;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;
import java.io.ByteArrayOutputStream;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/tasks")
public class TaskResource {

    @ConfigProperty(name = "tasknotes.gcp.project")
    String projectId;
    @ConfigProperty(name = "tasknotes.gcp.bucket")
    String bucketName;

    @Inject
    TaskService taskService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public List<Task> list() throws IOException, InterruptedException, ExecutionException {
        var tasks = taskService.findTasks();
        return tasks;
    }

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/image")
    public Response imageUpload(MultipartFormDataInput form) throws IOException {
        var image = new ImageFormBean(form.getFormDataMap());

        var storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var blobId = BlobId.of(bucketName, "image/" + UUID.randomUUID().toString() + ".png");
        var blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, image.getFile());

        return Response.ok(Map.of("url", "https://storage.googleapis.com/" + bucketName + "/" + blobInfo.getName()))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response save(TaskDocument document) throws IOException {
        var md = document.getText();

        var storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var blobId = BlobId.of(bucketName, "data/tasks.md");
        var blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, md.getBytes());

        System.out.println(new Date() + ": autosave");
        return Response.ok(Map.of("message", "success"))
                .build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/load")
    public TaskDocument load() throws IOException {

        var storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var blobId = BlobId.of(bucketName, "data/tasks.md");

        var output = new ByteArrayOutputStream();
        storage.get(blobId).downloadTo(output);

        var md = new String(output.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);

        System.out.println(new Date() + ": autosave");
        return new TaskDocument(md);
    }
}
