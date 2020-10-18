/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apps;

import java.util.Scanner;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author koduki
 */
@Provider
public class NotFoundExeptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        var errorPage = "index.html";
        var text = new Scanner(this.getClass().getResourceAsStream("/META-INF/resources/" + errorPage), "UTF-8").useDelimiter("\\A").next();
        return Response.status(404).entity(text).build();
    }
}
