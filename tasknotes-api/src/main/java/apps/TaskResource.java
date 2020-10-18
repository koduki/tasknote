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
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/tasks")
public class TaskResource {

    @Inject
    JsonWebToken jwt;

    @ConfigProperty(name = "tasknotes.gcp.project")
    String projectId;
    @ConfigProperty(name = "tasknotes.gcp.bucket.image")
    String imageBucket;
    @ConfigProperty(name = "tasknotes.gcp.bucket.data")
    String dataBucket;

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/image")
    public Response imageUpload(@Context SecurityContext ctx, MultipartFormDataInput form) throws IOException {
        var userId = ctx.getUserPrincipal().getName();
        var image = new ImageFormBean(form.getFormDataMap());

        var storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var blobId = BlobId.of(imageBucket, userId + "/" + UUID.randomUUID().toString() + ".png");
        var blobInfo = BlobInfo.newBuilder(blobId).build();

        storage.create(blobInfo, image.getFile());

        return Response.ok(Map.of("url", "https://storage.googleapis.com/" + imageBucket + "/" + blobInfo.getName()))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response save(@Context SecurityContext ctx, TaskDocument document) throws IOException {
        var userId = ctx.getUserPrincipal().getName();
        var md = document.getText();

        var storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var blobId = BlobId.of(dataBucket, userId + "/" + "tasks.md");
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
    public TaskDocument load(@Context SecurityContext ctx) throws IOException {
        var userId = ctx.getUserPrincipal().getName();

        var storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        var blobId = BlobId.of(dataBucket, userId + "/" + "tasks.md");

        var output = new ByteArrayOutputStream();
        storage.get(blobId).downloadTo(output);

        var md = new String(output.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);

        System.out.println(new Date() + ": autosave");
        return new TaskDocument(md);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/healthcheck")
    public Response healthcheck() throws IOException {

        return Response.ok(new Date()).build();
    }
}
