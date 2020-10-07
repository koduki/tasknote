package apps;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.StorageOptions;
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

    @ConfigProperty(name = "todo.gcp.project")
    String projectId;
    String bucketName = "ajfgeay8733";

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
    public Response save() throws IOException {
        System.out.println("heartbeat: " + new Date());
        return Response.ok(Map.of("message", "success"))
                .build();
    }
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    @Path("/create")
//    public TemplateInstance create() throws InterruptedException, ExecutionException {
//        return Templates.create(new HomeControllerHelper(), toolboxService.getTags());
//    }
////
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    @Path("/edit/{id}")
//    public TemplateInstance edit(@PathParam("id") String id) throws InterruptedException, ExecutionException {
//        return Templates.edit(new HomeControllerHelper(), toolboxService.getTags(), toolboxService.getItem(id));
//    }

//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Produces(MediaType.TEXT_PLAIN)
//    @Path("/doCreate")
//    public Response doCreate(@FormParam("document") String document)
//            throws InterruptedException, ExecutionException, URISyntaxException {
//
//        var tasks = parseTasks(document);
//
//        System.out.println(tasks);
//        var results = new ArrayList<ApiFuture>();
//        for (var task : tasks) {
//            results.add(toolboxService.createTask(task));
//        }
//
//        for (var r : results) {
//            r.get();
//        }
//
//        return Response
//                .seeOther(new URI("/"))
//                .build();
//    }
//    @POST
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    @Path("/doUpdate")
//    public Response doUpdate(
//            @FormParam("id") String id,
//            @FormParam("name") String name,
//            @FormParam("url") String url,
//            @FormParam("type") String type,
//            @FormParam("tags") String tags,
//            @FormParam("description") String description,
//            @FormParam("details") String details
//    ) throws InterruptedException, ExecutionException, URISyntaxException {
//        var result = toolboxService.updateItem(new Task(id, name, type, tags, description, url, details));
//        System.out.println("Update time : " + result.get().getUpdateTime());
//
//        return Response
//                .seeOther(new URI("/show/" + id))
//                .build();
//    }
//    ArrayList<Task> parseTasks(String document) {
//        var tasks = new ArrayList<Task>();
//        String name = "";
//        StringBuilder body = null;
//        for (var line : document.lines().collect(Collectors.toList())) {
//            if (line.trim().startsWith("# ")) {
//                if (body != null) {
//                    var task = new Task(null, name, body.toString());
//                    tasks.add(task);
//                }
//                name = line.replaceFirst("# ", "");
//
//                body = null;
//            } else if (line.trim().startsWith("## body")) {
//                body = new StringBuilder();
//            } else if (body != null) {
//                body.append(line);
//                body.append("\n");
//            }
//        }
//        tasks.add(new Task(null, name, body.toString()));
//        return tasks;
//    }
}
