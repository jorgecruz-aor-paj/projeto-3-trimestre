package aor.proj3t.resource;

import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.service.ServiceFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/workplaces")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorkplaceResource {

    @Inject
    ServiceFactory serviceFactory;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkplaces() {
        List<WorkplaceDTO> list = serviceFactory.forWorkplace().getAllWorkplaces();
        return Response.ok(list).build();
    }
}
