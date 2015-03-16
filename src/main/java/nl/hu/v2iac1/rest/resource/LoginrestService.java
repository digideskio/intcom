package nl.hu.v2iac1.rest.resource;

import javax.servlet.http.HttpServletRequest;
import nl.hu.v2iac1.Configuration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
@Produces(MediaType.TEXT_PLAIN)
public class LoginrestService extends AbstractRestService {
    @Context private HttpServletRequest request;

    @GET
    @Path("/")
    public Response getSecret() {

        String output = "login page";
        return Response.status(200).entity(output).build();

    }
}
