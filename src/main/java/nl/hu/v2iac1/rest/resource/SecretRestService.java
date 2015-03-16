package nl.hu.v2iac1.rest.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import nl.hu.v2iac1.Configuration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/secret")
@Produces(MediaType.TEXT_PLAIN)
public class SecretRestService extends AbstractRestService{



    @GET
    @Path("/")
    public Response getSecret(@Context HttpServletRequest req) {
        
        HttpSession session = req.getSession();
    	String username = session.getAttribute("username").toString();
    	String logintoken = session.getAttribute("logintoken").toString();
            String output = "Hello "+username+", this is the secret: " + configuration.getValue(Configuration.Key.SECRET);
            return Response.status(200).entity(output).build();
    }

}