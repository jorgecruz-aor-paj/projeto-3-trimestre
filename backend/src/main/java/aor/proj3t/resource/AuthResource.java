package aor.proj3t.resource;

import aor.proj3t.dto.UserRegistrationDTO;
import jakarta.ws.rs.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.ValidatorUtil;
import aor.proj3t.util.authutils.Secured;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST endpoint for authentication-related operations.
 * This class is responsible for handling requests related to user
 * authentication,
 * such as login and logout operations.
 */
@Path("/auth")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {
    private static final Logger logger = LogManager.getLogger(AuthResource.class);

    @Context
    private HttpHeaders headers;

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    ValidatorUtil validatorUtil;

    @Inject
    JwtUtil jwtUtil;

    @POST
    @Path("/register")
    // Public endpoint - no @Secured annotation
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(LoginDTO loginDto) {
        try {
            String ipAddress = headers.getHeaderString("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = headers.getHeaderString("X-Real-IP");
            }
            if (!validatorUtil.isValidLoginDTO(loginDto)) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Invalid data")).build();
            }

            Boolean success = serviceFactory.forUser().registerUser(loginDto, ipAddress);
            if (success) {
                return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
            } else {
                return Response.status(Response.Status.CONFLICT).entity(new ErrorResponse("Existing registration email")).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Unexpected error"))
                    .build();
        }
    }

    @POST
    @Path("/login")
    @Secured
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

            String sessionToken = serviceFactory.forUser().login(userId, loginDto, ipAddress);
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

    @POST
    @Path("/confirm")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmUser(@QueryParam("confirmationToken") String confirmationToken,
            UserRegistrationDTO userRegistrationDTO) {
        String ipAddress = headers.getHeaderString("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = headers.getHeaderString("X-Real-IP");
        }
        logger.info("Received confirmation request for token: {}", confirmationToken);
        if (!validatorUtil.isValidUserRegistrationDTO(userRegistrationDTO)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Invalid data")).build();
        }
        try {
            if (!jwtUtil.validateToken(confirmationToken)) {
                logger.warn("Invalid token");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Invalid or expired token")).build();
            }
            Claims claims = jwtUtil.extractAllClaims(confirmationToken);
            String email = claims.get("email").toString();
            Boolean success = serviceFactory.forUser().confirmUser(email, userRegistrationDTO);
            if (success) {
                return Response.status(Response.Status.OK).entity("User confirmed successfully").build();
            } else {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(new ErrorResponse("Unexpected error")).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorResponse("Unexpected error"))
                    .build();
        }
    }

    @GET
    @Path("/resetPassword/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResetPasswordLink(@PathParam("email") String email) {
        String ipAddress = headers.getHeaderString("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = headers.getHeaderString("X-Real-IP");
        }
        logger.info("Received reset password request for email: {}", email);
        if (!validatorUtil.isValidEmail(email)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorResponse("Invalid data")).build();
        }
        return Response.status(Response.Status.OK).build();
    }
}