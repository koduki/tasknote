package apps;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@Path("/tasks")
public class TasksResource {

    private static final Logger LOGGER = Logger.getLogger(TasksResource.class.getName());

    @Inject
    JsonWebToken jwt;

    @Inject
    TasksService tasksService;

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/image")
    public Response uploadImage(@Context SecurityContext ctx, MultipartFormDataInput form) throws IOException {
        var userId = ctx.getUserPrincipal().getName();

        var uploadedUrl = tasksService.uploadImage(userId, new ImageFormBean(form.getFormDataMap()));

        return Response.ok(uploadedUrl)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/save")
    public Response save(@Context SecurityContext ctx, TasksDocument document) throws IOException {
        var userId = ctx.getUserPrincipal().getName();

        tasksService.saveNote(document, userId);
        LOGGER.fine("save");
        return Response.ok(Map.of("message", "success"))
                .build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/load")
    public Response load(@Context SecurityContext ctx) throws IOException {
        var userId = ctx.getUserPrincipal().getName();
        var noteName = "tasks.md";

        var doc = tasksService.loadNote(userId, noteName);
        if (doc.isPresent()) {
            return Response.
                    ok(new ObjectMapper().writeValueAsString(doc.get()))
                    .build();
        } else {
            LOGGER.fine("init data");
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/healthcheck")
    public Response healthcheck() throws IOException {
        return Response.ok(new Date()).build();
    }
}
