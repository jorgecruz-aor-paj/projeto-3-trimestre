package aor.proj3t.resource;

import java.util.List;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.dto.ManagerAssignDTO;
import aor.proj3t.dto.MiniUserDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.UserFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import aor.proj3t.util.validatorutils.fordto.UserValidator;
import aor.proj3t.util.validatorutils.forfilters.FilterValidatorFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST endpoint for user-related operations.
 * <p>
 * This class manages requests pertaining to user entities,
 * including profile retrieval, user updates, manager assignments,
 * and compact listings. It applies role-based access control
 * and delegates processing to the service layer with appropriate validation.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Path("/users")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private ServiceFactory serviceFactory;

    @Inject
    private UserValidator validator;

    @Inject
    private FilterValidatorFactory validatorFactory;

    /**
     * Retrieves profile information of the authenticated user.
     *
     * @param authHeader the bearer token from the authorization header
     * @return the authenticated user's details as a UserDTO
     */
    @GET
    @Path("/me")
    @Secured
    public Response getMe(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        UserDTO user = serviceFactory.forUser().getUser(userId);
        return Response.ok(user).build();
    }

    /**
     * Retrieves a list of users filtered by query parameters.
     *
     * @param authHeader the bearer token from the authorization header
     * @param filters the applied filters for user search
     * @return a filtered list of UserDTOs
     */
    @GET
    @Secured
    public Response getUsers(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                             @BeanParam UserFilter filters) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        validatorFactory.validate(filters);
        List<UserDTO> users = serviceFactory.forUser().getUsersByFilters(requesterId, filters);
        return Response.ok(users).build();
    }

    /**
     * Updates an existing user profile with new information.
     *
     * @param authHeader the bearer token from the authorization header
     * @param userDTO the updated user data
     * @return the updated UserDTO
     */
    @PUT
    @Secured
    public Response updateUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                               UserDTO userDTO) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        validator.validateUserDTO(userDTO);
        serviceFactory.forUser().updateUser(requesterId, userDTO);
        return Response.ok(userDTO).build();
    }

    /**
     * Assigns a manager to multiple users in bulk.
     *
     * @param authHeader the bearer token from the authorization header
     * @param managerAssignDto DTO containing manager and user assignment data
     * @return response confirming successful assignment
     */
    @PATCH
    @Secured(Role.ADMIN)
    public Response assignManager(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                  ManagerAssignDTO managerAssignDto) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        validator.validateManagerAssignDTO(managerAssignDto);
        serviceFactory.forUser().assignManagerBulk(requesterId, managerAssignDto);
        return Response.ok(new APIResponseDTO("Managers assigned successfully")).build();
    }

    /**
     * Retrieves all users with the role of manager.
     *
     * @param authHeader the bearer token from the authorization header
     * @return a list of managers as UserDTOs
     */
    @GET
    @Path("/managers")
    @Secured(Role.ADMIN)
    public Response getManagers(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<UserDTO> managers = serviceFactory.forUser().getAllManagers(requesterId);
        return Response.ok(managers).build();
    }

    /**
     * Retrieves a compact list of users, optionally excluding admins.
     *
     * @param includeAdmins flag indicating whether admins should be included
     * @return list of minimal user representations
     */
    @GET
    @Path("/compact")
    @Secured
    public Response getCompactUsers(@QueryParam("includeAdmins") boolean includeAdmins) {
        List<MiniUserDTO> list = serviceFactory.forUser().getAllMiniUsers(includeAdmins);
        return Response.ok(list).build();
    }
}

