package aor.proj3t.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.dto.UserRegistrationDTO;
import aor.proj3t.dto.auth.LoginResponseDTO;
import aor.proj3t.dto.auth.LoginTwoFactorVerificationDTO;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.JwtUtil;
import aor.proj3t.util.authutils.TokenUtil;
import aor.proj3t.util.validatorutils.fordto.UserValidator;
import io.jsonwebtoken.Claims;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
/**
 * REST endpoint for authentication-related operations.
 * <p>
 * This class is responsible for handling requests related to user authentication,
 * including registration, login (with optional two-factor), logout, confirmation,
 * and password reset. It delegates logic to service-layer components and ensures
 * validation and security headers are respected.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
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
    private ServiceFactory serviceFactory;

    @Inject
    private UserValidator validator;

    @Inject
    private JwtUtil jwtUtil;

    /**
     * Registers a new user using credentials provided in the LoginDTO.
     * Validates input and captures IP address for audit.
     *
     * @param loginDto the user credentials and email
     * @return a response indicating successful registration
     */
    @POST
    @Path("/register")
    public Response registerUser(LoginDTO loginDto) {
        String ipAddress = ThreadContext.get("clientIp");
        validator.validateLoginDTO(loginDto);
        serviceFactory.forUser().registerUser(loginDto, ipAddress);
        return Response.status(Response.Status.CREATED)
                .entity(new APIResponseDTO("User registered successfully"))
                .build();
    }

    /**
     * Authenticates a user using provided login credentials.
     * Handles IP and User-Agent headers for session context.
     *
     * @param loginDto the login credentials
     * @return a response containing session or temporary token data
     */
    @POST
    @Path("/login")
    public Response login(LoginDTO loginDto) {
        validator.validateLoginDTO(loginDto);
        String userAgent = headers.getHeaderString("User-Agent");
        String ipAddress = ThreadContext.get("clientIp");
        LoginResponseDTO loginResponse = serviceFactory.forAuth().login(loginDto, ipAddress, userAgent);
        return Response.ok(loginResponse).build();
    }

    /**
     * Completes a login request that requires two-factor authentication.
     *
     * @param verificationDto DTO containing temporary token and 2FA code
     * @return session token upon successful two-factor verification
     */
    @POST
    @Path("/complete-2fa")
    public Response completeTwoFactorLogin(LoginTwoFactorVerificationDTO verificationDto) {
        String userAgent = headers.getHeaderString("User-Agent");
        String ipAddress = ThreadContext.get("clientIp");
        String sessionToken = serviceFactory.forAuth().completeTwoFactorLogin(
                verificationDto.getTemporaryToken(),
                verificationDto.getCode(),
                ipAddress,
                userAgent);
        return Response.ok(sessionToken).build();
    }

    /**
     * Logs the user out by invalidating the session token.
     *
     * @param authHeader the authorization header containing the bearer token
     * @return a response confirming logout success
     */
    @POST
    @Path("/logout")
    public Response logout(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = TokenUtil.extractToken(authHeader);
        Long userId = serviceFactory.forToken().getUserIdFromToken(token);
        serviceFactory.forAuth().logout(userId, token);
        return Response.ok(new APIResponseDTO("Logout successful")).build();
    }

    /**
     * Confirms a newly registered user using a confirmation token and registration data.
     *
     * @param confirmationToken the confirmation JWT token
     * @param userRegistrationDTO user profile details for confirmation
     * @return a response indicating successful confirmation
     */
    @POST
    @Path("/confirm")
    public Response confirmUser(@QueryParam("confirmationToken") String confirmationToken,
                                UserRegistrationDTO userRegistrationDTO) {
        if (!jwtUtil.validateToken(confirmationToken)) {
            logger.warn("Invalid or expired confirmation token");
            throw new UnauthorizedException("Invalid or expired token");
        }
        validator.validateUserRegistrationDTO(userRegistrationDTO);
        Claims claims = jwtUtil.extractAllClaims(confirmationToken);
        String email = claims.get("email").toString();

        serviceFactory.forUser().confirmUser(email, userRegistrationDTO);
        return Response.ok(new APIResponseDTO("User confirmed successfully")).build();
    }

    /**
     * Sends a password reset link to the user with the specified email.
     * Always returns a generic message to prevent user enumeration.
     *
     * @param email the email address to send reset instructions to
     * @return a response indicating email was sent
     */
    @GET
    @Path("/resetPassword/{email}")
    public Response getResetPasswordLink(@PathParam("email") String email) {
        logger.info("Received reset password request for email: {}", email);
        String ipAddress = ThreadContext.get("clientIp");
        validator.validateEmail(email);
        serviceFactory.forUser().sendResetPasswordLink(email, ipAddress);
        return Response.ok(new APIResponseDTO("Email sent. Check your inbox")).build();
    }

    /**
     * Completes the password reset flow using a reset token and new password.
     *
     * @param resetPasswordToken JWT token included in reset link
     * @param loginDTO DTO containing the new password
     * @return a response confirming password change
     */
    @PATCH
    @Path("/resetPassword")
    public Response resetPassword(@QueryParam("resetPasswordToken") String resetPasswordToken,
                                  LoginDTO loginDTO) {
        validator.validatePassword(loginDTO.getPassword());
        serviceFactory.forUser().changePassword(resetPasswordToken, loginDTO);
        return Response.ok(new APIResponseDTO("User password changed successfully")).build();
    }
}
