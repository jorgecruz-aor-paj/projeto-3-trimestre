package aor.proj3t.resource;

import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.ValidatorUtil;
import aor.proj3t.util.authutils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * REST endpoint for user-related operations.
 * This class is responsible for handling requests related to user management.
 * It includes operations such as user registration, profile management, and
 * user-related queries.
 */
@Path("/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private static final Logger logger = LogManager.getLogger(UserResource.class);

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    ValidatorUtil validatorUtil;


    @GET
    @Path("/me")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMe(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long userId = serviceFactory.forAuth().getUserIdFromAuthHeader(authHeader);
        UserDTO user = serviceFactory.forUser().getUser(userId);
        return Response.ok(user).build();
    }

    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, UserDTO userDTO) {
        Long requesterId = serviceFactory.forAuth().getUserIdFromAuthHeader(authHeader);
        validatorUtil.validateUserDTO(userDTO);
        serviceFactory.forUser().updateUser(requesterId, userDTO);
        return Response.ok(userDTO).build();
    }

    @PATCH
    @Secured(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignManager(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, ManagerAssignDTO managerAssignDto) {
        Long requesterId = serviceFactory.forAuth().getUserIdFromAuthHeader(authHeader);
        validatorUtil.validateManagerAssignDTO(managerAssignDto);
        serviceFactory.forUser().assignManagerBulk(requesterId,managerAssignDto);
        return Response.ok("Managers assigned successfully").build();
    }

}

