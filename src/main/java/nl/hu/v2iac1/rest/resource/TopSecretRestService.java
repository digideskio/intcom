package nl.hu.v2iac1.rest.resource;

import nl.hu.v2iac1.Configuration;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/topsecret")
@Produces(MediaType.TEXT_PLAIN)
public class TopSecretRestService extends AbstractRestService {

    @GET
    @Path("/")
    public Response getSecret() {

        String output = "This is TOP secret: " + configuration.getValue(Configuration.Key.TOPSECRET);
        return Response.status(200).entity(output).build();

    }

}