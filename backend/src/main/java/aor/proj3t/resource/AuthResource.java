package aor.proj3t.resource;

import aor.proj3t.exceptions.UnauthorizedException;
import jakarta.ws.rs.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.JwtUtil;
import aor.proj3t.util.ValidatorUtil;
import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(LoginDTO loginDto) {
        String ipAddress = ThreadContext.get("clientIp");
        validatorUtil.validateLoginDTO(loginDto);
        serviceFactory.forUser().registerUser(loginDto, ipAddress);
        return Response.status(Response.Status.CREATED)
                .entity("User registered successfully")
                .build();
    }


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO loginDto) {
        validatorUtil.validateLoginDTO(loginDto);
        String userAgent = headers.getHeaderString("User-Agent");
        String ipAddress = ThreadContext.get("clientIp");
        String sessionToken = serviceFactory.forAuth().login(loginDto, ipAddress, userAgent);
        return Response.ok(sessionToken).build();
    }


    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponse("Missing or malformed token"))
                    .build();
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        Long userId = serviceFactory.forAuth().getUserIdFromAuthHeader(token);
        serviceFactory.forAuth().logout(userId, token);
        return Response.ok("Logout successful").build();
    }


    @POST
    @Path("/confirm")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmUser(@QueryParam("confirmationToken") String confirmationToken,
                                UserRegistrationDTO userRegistrationDTO) {
        logger.info("Received confirmation request for token: {}", confirmationToken);
        if (!jwtUtil.validateToken(confirmationToken)) {
            logger.warn("Invalid or expired confirmation token");
            throw new UnauthorizedException("Invalid or expired token");
        }
        validatorUtil.validateUserRegistrationDTO(userRegistrationDTO);
        Claims claims = jwtUtil.extractAllClaims(confirmationToken);
        String email = claims.get("email").toString();

        serviceFactory.forUser().confirmUser(email, userRegistrationDTO);

        return Response.ok("User confirmed successfully").build();
    }


    @GET
    @Path("/resetPassword/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResetPasswordLink(@PathParam("email") String email) {
        logger.info("Received reset password request for email: {}", email);
        String ipAddress = ThreadContext.get("clientIp");
        validatorUtil.validateEmail(email);
        serviceFactory.forUser().sendResetPasswordLink(email, ipAddress);

        // Always return the same message to prevent email enumeration
        return Response.ok(new ErrorResponse("Email sent. Check your inbox")).build();
    }


    @PATCH
    @Path("/resetPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(@QueryParam("resetPasswordToken") String resetPasswordToken,
                                  LoginDTO loginDTO) {
        logger.info("Received reset password for token: {}", resetPasswordToken);

        if (!jwtUtil.validateToken(resetPasswordToken)) {
            logger.warn("Invalid or expired reset Password token");
            throw new UnauthorizedException("Invalid or expired token");
        }
        validatorUtil.validateLoginDTO(loginDTO);
        Claims claims = jwtUtil.extractAllClaims(resetPasswordToken);
        Long userId = Long.parseLong(claims.get("userId").toString());
        serviceFactory.forUser().changePassword(userId, loginDTO.getPassword());
        return Response.ok("User password changed successfully").build();
    }


}