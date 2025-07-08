package aor.proj3t.resource;

import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.validatorutils.fordto.UserValidator;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
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
