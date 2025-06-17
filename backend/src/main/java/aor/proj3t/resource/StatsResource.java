package aor.proj3t.resource;

import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.Secured;
import aor.proj3t.util.validatorutils.fordto.UserValidator;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;


@Path("/stats")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatsResource {

    @Context
    private HttpHeaders headers;

    @Inject
    ServiceFactory serviceFactory;


    @GET
    @Path("/managerAssignment")
    @Secured(Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManagerAssignment(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        Map<Long,Long> map = serviceFactory.forUser().getTotalVSUnmanagedUsers(requesterId);
        return Response.ok(map).build();
    }

}

