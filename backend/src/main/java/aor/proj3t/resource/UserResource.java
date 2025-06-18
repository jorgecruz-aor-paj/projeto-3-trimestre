package aor.proj3t.resource;

import jakarta.ws.rs.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.validatorutils.fordto.UserValidator;
import aor.proj3t.util.authutils.Secured;
import aor.proj3t.util.validatorutils.fordto.UserValidator;
import aor.proj3t.util.validatorutils.forfilters.FilterValidatorFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

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
    UserValidator validator;

    @Inject
    FilterValidatorFactory validatorFactory;

    @GET
    @Path("/me")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMe(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        UserDTO user = serviceFactory.forUser().getUser(userId);
        return Response.ok(user).build();
    }

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, @BeanParam UserFilter filters) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        validatorFactory.validate(filters);
        List<UserDTO> users = serviceFactory.forUser().getUsersByFilters(requesterId, filters);
        return Response.ok(users).build();
    }

    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, UserDTO userDTO) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        validator.validateUserDTO(userDTO);
        serviceFactory.forUser().updateUser(requesterId, userDTO);
        return Response.ok(userDTO).build();
    }

    @PATCH
    @Secured(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response assignManager(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            ManagerAssignDTO managerAssignDto) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        validator.validateManagerAssignDTO(managerAssignDto);
        serviceFactory.forUser().assignManagerBulk(requesterId, managerAssignDto);
        return Response.ok(new MessageResponse("Managers assigned successfully")).build();
    }

    @GET
    @Secured(Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManagers(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<UserDTO> managers = serviceFactory.forUser().getAllManagers(requesterId);
        return Response.ok(managers).build();
    }
}
