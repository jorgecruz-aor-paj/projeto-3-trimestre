package aor.proj3t.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.EmailTestDTO;
import aor.proj3t.util.EmailUtil;
import aor.proj3t.util.JwtUtil;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST endpoint for testing email-related functionality.
 * 
 * This class provides endpoints to test various email features including
 * general emails, confirmation emails, and password reset emails.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 */
@Path("/email-test")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmailTestResource {

        private static final Logger logger = LogManager.getLogger(EmailTestResource.class);

        @Inject
        private EmailUtil emailUtil;

        @Inject
        private JwtUtil jwtUtil;

        /**
         * Send a test email based on the provided details.
         * 
         * @param emailTestDTO Contains details about the email test to be performed
         * @return HTTP response indicating success or failure
         */
        @POST
        @Path("/send")
        public Response sendTestEmail(EmailTestDTO emailTestDTO) {
                logger.info("Attempting to send test email to: {}", emailTestDTO.getRecipient());

                if (emailTestDTO.getRecipient() == null || emailTestDTO.getRecipient().isEmpty()) {
                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity("{\"error\": \"Recipient email is required\"}")
                                        .build();
                }

                if (!emailUtil.isValidEmail(emailTestDTO.getRecipient())) {
                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity("{\"error\": \"Invalid email format\"}")
                                        .build();
                }

                boolean emailSent = false;

                try {
                        switch (emailTestDTO.getTestType() != null ? emailTestDTO.getTestType().toLowerCase()
                                        : "simple") {
                                case "confirmation":
                                        String confirmationToken = jwtUtil.generateConfirmationToken(
                                                        emailTestDTO.getRecipient(),
                                                        "127.0.0.1", // Sample IP address
                                                        null // Use default expiration
                                        );
                                        emailSent = emailUtil.sendConfirmationEmail(
                                                        emailTestDTO.getRecipient(),
                                                        confirmationToken,
                                                        emailTestDTO.getBaseUrl() != null ? emailTestDTO.getBaseUrl()
                                                                        : "http://localhost:8080");
                                        logger.info("Sent confirmation email with token: {}", confirmationToken);
                                        break;

                                case "reset":
                                        String resetToken = jwtUtil.generatePasswordResetToken(
                                                        emailTestDTO.getRecipient(),
                                                        1L,
                                                        "127.0.0.1", // Sample IP address
                                                        null // Use default expiration
                                        );
                                        emailSent = emailUtil.sendPasswordResetEmail(
                                                        emailTestDTO.getRecipient(),
                                                        resetToken,
                                                        emailTestDTO.getBaseUrl() != null ? emailTestDTO.getBaseUrl()
                                                                        : "http://localhost:8080");
                                        logger.info("Sent password reset email with token: {}", resetToken);
                                        break;

                                default: // "simple" or any other value
                                        String subject = emailTestDTO.getSubject() != null ? emailTestDTO.getSubject()
                                                        : "Test Email";
                                        String message = emailTestDTO.getMessage() != null ? emailTestDTO.getMessage()
                                                        : "This is a test email from the application.";

                                        // Create an HTML body using the template
                                        String body = emailUtil.createEmailTemplate(
                                                        "Email Test",
                                                        message,
                                                        "Visit Website",
                                                        "http://localhost:8080",
                                                        "This is an automatically generated test email.");

                                        emailSent = emailUtil.sendEmail(emailTestDTO.getRecipient(), subject, body);
                                        logger.info("Sent simple test email with subject: {}", subject);
                        }

                        if (emailSent) {
                                return Response
                                                .status(Response.Status.OK)
                                                .entity("{\"message\": \"Email sent successfully\"}")
                                                .build();
                        } else {
                                return Response
                                                .status(Response.Status.INTERNAL_SERVER_ERROR)
                                                .entity("{\"error\": \"Failed to send email\"}")
                                                .build();
                        }
                } catch (Exception e) {
                        logger.error("Error sending test email", e);
                        return Response
                                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity("{\"error\": \"" + e.getMessage() + "\"}")
                                        .build();
                }
        }

        /**
         * Simple endpoint to check if the email test service is up and running.
         * 
         * @return OK response with a message
         */
        @POST
        @Path("/ping")
        // No @Secured annotation = publicly accessible
        public Response ping() {
                return Response
                                .status(Response.Status.OK)
                                .entity("{\"message\": \"Email test service is running\"}")
                                .build();
        }
}
