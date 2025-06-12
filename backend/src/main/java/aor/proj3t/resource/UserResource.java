package aor.proj3t.resource;

import aor.proj3t.dto.UserDTO;
import aor.proj3t.entities.User;
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
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("Invalid token")).build();
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        try {
            if (!serviceFactory.forAuth().isValidToken(token)) {
                logger.warn("Invalid or expired token");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Invalid or expired token")).build();
            }
            Claims claims = jwtUtil.extractAllClaims(token);
            Long userId = Long.parseLong(claims.get("userId").toString());
            UserDTO user = serviceFactory.forUser().getUser(userId);
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("Invalid user")).build();
            } else {
                return Response.status(Response.Status.OK).entity(user).build();
            }
        } catch (Exception e) {
            logger.error("Invalid or expired token", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("").build();
        }

    }
}

