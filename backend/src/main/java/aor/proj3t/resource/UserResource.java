package aor.proj3t.resource;

import aor.proj3t.dto.UserDTO;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.ValidatorUtil;
import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
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


    @Context
    private HttpHeaders headers;

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    ValidatorUtil validatorUtil;

    @Inject
    JwtUtil jwtUtil;

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMe(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or malformed token");
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        logger.info("Received request to get own user info with token {}", token);
        Long userId = serviceFactory.forAuth().getUserIdFromAuthHeader(authHeader);

        UserDTO user = serviceFactory.forUser().getUser(userId);
        return Response.ok(user).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, UserDTO userDTO) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or malformed token");
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        logger.info("Received request to update user info with token {}", token);
        if (!serviceFactory.forAuth().isValidToken(token)) {
            throw new UnauthorizedException("Invalid or expired token");
        }
        if (!validatorUtil.isValidUserDTO(userDTO)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Invalid data"))
                    .build();
        }
        Long requesterId = serviceFactory.forAuth().getUserIdFromToken(token);
        serviceFactory.forUser().updateUser(requesterId, userDTO);
        return Response.ok(userDTO).build();
    }


}

