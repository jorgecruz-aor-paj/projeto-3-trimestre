package aor.proj3t.resource;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.ValidatorUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@HeaderParam("Authorization") String authHeader, LoginDTO loginDto) {
        String ipAddress = headers.getHeaderString("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = headers.getHeaderString("X-Real-IP");
        }

        if (!validatorUtil.isValidLoginDTO(loginDto)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Invalid data")).build();
        }
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorResponse("Invalid token")).build();
        }

        String token = authHeader.substring("Bearer ".length()).trim();

        try {
            if (!jwtUtil.validateToken(token)) {
                logger.warn("Invalid token");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Invalid or expired token")).build();
            }
            Claims claims = jwtUtil.extractAllClaims(token);
            Long userId = Long.parseLong(claims.get("userId").toString());
            String role = claims.get("role", String.class);

            UserDTO user = serviceFactory.forUser().getUser(userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse("Invalid token userId"))
                        .build();
            }

            if (!user.getRole().equals(Role.valueOf(role))) {
                return Response.status(Response.Status.FORBIDDEN).entity(new ErrorResponse("Invalid token role"))
                        .build();
            }

            String sessionToken = serviceFactory.forUser().login(loginDto, ipAddress);
            if (sessionToken != null && !sessionToken.isEmpty()) {
                logger.info("Login successful for user with id {}", userId);
                return Response.status(Response.Status.OK).entity(sessionToken).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Invalid credentials"))
                        .build();
            }

        } catch (JwtException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Invalid credentials"))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Unexpected error"))
                    .build();
        }
    }

}
