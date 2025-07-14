package aor.proj3t.resource;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.auth.BackupCodesResponseDTO;
import aor.proj3t.dto.auth.DisableTwoFactorDTO;
import aor.proj3t.dto.auth.ErrorResponseDTO;
import aor.proj3t.dto.auth.SuccessResponseDTO;
import aor.proj3t.dto.auth.TwoFactorSetupDTO;
import aor.proj3t.dto.auth.TwoFactorStatusResponseDTO;
import aor.proj3t.dto.auth.TwoFactorVerificationDTO;
import aor.proj3t.exceptions.TwoFactorAuthException;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.TwoFactorAuthService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST resource for Two-Factor Authentication operations.
 * Provides endpoints for 2FA setup, verification, and management.
 * 
 * @author Jorge Cruz
 */
@RequestScoped
@Path("/auth/2fa")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TwoFactorAuthResource {

    private static final Logger logger = LogManager.getLogger(TwoFactorAuthResource.class);

    @Inject
    private ServiceFactory serviceFactory;

    /**
     * Initiates 2FA setup for the authenticated user.
     * Generates a secret key, backup codes, and QR code URL.
     * 
     * @param authHeader the Authorization header containing JWT token
     * @return TwoFactorSetupDTO with setup information
     */
    @POST
    @Path("/setup")
    public Response setupTwoFactorAuth(@HeaderParam("Authorization") String authHeader) {
        try {
            // Extract user ID from JWT token using TokenService
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Setup 2FA
            TwoFactorAuthService.TwoFactorSetupData setupData = serviceFactory.forTwoFactorAuth()
                    .setupTwoFactorAuth(userId);

            // Create response DTO
            TwoFactorSetupDTO response = new TwoFactorSetupDTO(
                    setupData.getSecretKey(),
                    setupData.getQrCodeUrl(),
                    setupData.getBackupCodes());

            logger.info("2FA setup initiated for user: {}", userId);
            return Response.ok(response).build();

        } catch (UnauthorizedException e) {
            logger.warn("Authentication failed during 2FA setup: {}", e.getUserMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (TwoFactorAuthException e) {
            logger.warn("2FA setup failed: {}", e.getUserMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error during 2FA setup", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponseDTO("Internal server error"))
                    .build();
        }
    }

    /**
     * Verifies a TOTP code and enables 2FA if verification is successful.
     * This is for the initial setup process.
     * 
     * @param authHeader      the Authorization header containing JWT token
     * @param verificationDTO the verification request containing the TOTP code
     * @return success response if verification successful
     */
    @POST
    @Path("/setup/verify")
    public Response verifyAndEnableTwoFactorAuth(
            @HeaderParam("Authorization") String authHeader,
            TwoFactorVerificationDTO verificationDTO) {
        try {
            // Extract user ID from JWT token using TokenService
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Verify and enable 2FA
            boolean success = serviceFactory.forTwoFactorAuth().verifyAndEnableTwoFactorAuth(
                    userId, verificationDTO.getCode());

            if (success) {
                logger.info("2FA enabled successfully for user: {}", userId);
                return Response.ok(new SuccessResponseDTO("Two-factor authentication enabled successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponseDTO("Invalid verification code"))
                        .build();
            }

        } catch (UnauthorizedException e) {
            logger.warn("Authentication failed during 2FA verification: {}", e.getUserMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (TwoFactorAuthException e) {
            logger.warn("2FA verification failed: {}", e.getUserMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error during 2FA verification", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponseDTO("Internal server error"))
                    .build();
        }
    }

    /**
     * Verifies a 2FA code during login process.
     * This is for authenticating during the login flow.
     * 
     * @param authHeader      the Authorization header containing temporary JWT
     *                        token
     * @param verificationDTO the verification request containing the TOTP code or
     *                        backup code
     * @return success response if verification successful
     */
    @POST
    @Path("/login/verify")
    public Response verifyTwoFactorCode(
            @HeaderParam("Authorization") String authHeader,
            TwoFactorVerificationDTO verificationDTO) {
        try {
            // Extract user ID from JWT token using TokenService
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Verify 2FA code
            boolean success = serviceFactory.forTwoFactorAuth().verifyTwoFactorCode(
                    userId, verificationDTO.getCode());

            if (success) {
                logger.info("2FA login verification successful for user: {}", userId);
                return Response.ok(new SuccessResponseDTO("Two-factor authentication verified")).build();
            } else {
                logger.warn("2FA login verification failed for user: {}", userId);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponseDTO("Invalid authentication code"))
                        .build();
            }

        } catch (UnauthorizedException e) {
            logger.warn("Authentication failed during 2FA login verification: {}", e.getUserMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (TwoFactorAuthException e) {
            logger.warn("2FA login verification failed: {}", e.getUserMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error during 2FA login verification", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponseDTO("Internal server error"))
                    .build();
        }
    }

    /**
     * Disables 2FA for the authenticated user.
     * 
     * @param authHeader     the Authorization header containing JWT token
     * @param disableRequest the request containing password confirmation
     * @return success response if 2FA was disabled
     */
    @DELETE
    @Path("/disable")
    public Response disableTwoFactorAuth(
            @HeaderParam("Authorization") String authHeader,
            DisableTwoFactorDTO disableRequest) {
        try {
            // Extract user ID from JWT token using TokenService
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Disable 2FA
            boolean success = serviceFactory.forTwoFactorAuth().disableTwoFactorAuth(
                    userId, disableRequest.getPassword());

            if (success) {
                logger.info("2FA disabled successfully for user: {}", userId);
                return Response.ok(new SuccessResponseDTO("Two-factor authentication disabled")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponseDTO("Failed to disable two-factor authentication"))
                        .build();
            }

        } catch (UnauthorizedException e) {
            logger.warn("Authentication failed during 2FA disable: {}", e.getUserMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (TwoFactorAuthException e) {
            logger.warn("2FA disable failed: {}", e.getUserMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error during 2FA disable", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponseDTO("Internal server error"))
                    .build();
        }
    }

    /**
     * Regenerates backup codes for the authenticated user.
     * 
     * @param authHeader the Authorization header containing JWT token
     * @return new backup codes
     */
    @POST
    @Path("/backup-codes/regenerate")
    public Response regenerateBackupCodes(@HeaderParam("Authorization") String authHeader) {
        try {
            // Extract user ID from JWT token using TokenService
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Regenerate backup codes
            List<String> backupCodes = serviceFactory.forTwoFactorAuth().regenerateBackupCodes(userId);

            logger.info("Backup codes regenerated for user: {}", userId);
            return Response.ok(new BackupCodesResponseDTO(backupCodes)).build();

        } catch (UnauthorizedException e) {
            logger.warn("Authentication failed during backup codes regeneration: {}", e.getUserMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (TwoFactorAuthException e) {
            logger.warn("Backup codes regeneration failed: {}", e.getUserMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error during backup codes regeneration", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponseDTO("Internal server error"))
                    .build();
        }
    }

    /**
     * Checks if 2FA is enabled for the authenticated user.
     * 
     * @param authHeader the Authorization header containing JWT token
     * @return 2FA status
     */
    @GET
    @Path("/status")
    public Response getTwoFactorStatus(@HeaderParam("Authorization") String authHeader) {
        try {
            // Extract user ID from JWT token using TokenService
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Check 2FA status
            boolean isEnabled = serviceFactory.forTwoFactorAuth().isTwoFactorEnabled(userId);

            return Response.ok(new TwoFactorStatusResponseDTO(isEnabled)).build();

        } catch (UnauthorizedException e) {
            logger.warn("Authentication failed while checking 2FA status: {}", e.getUserMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorResponseDTO(e.getUserMessage()))
                    .build();
        } catch (Exception e) {
            logger.error("Unexpected error checking 2FA status", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponseDTO("Internal server error"))
                    .build();
        }
    }
}
