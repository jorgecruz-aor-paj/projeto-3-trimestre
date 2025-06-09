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
 * REST endpoint for testing application features.
 * 
 * This class provides endpoints to test various application features
 * without affecting production data.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 */
@Path("/test")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestResource {

        private static final Logger logger = LogManager.getLogger(TestResource.class);

        @Inject
        private EmailUtil emailUtil;

        @Inject
        private JwtUtil jwtUtil;

        /**
         * Test endpoint to check if the service is up and running.
         * 
         * @return A simple response indicating the service is working
         */
        @POST
        @Path("/ping")
        public Response ping() {
                logger.info("Test ping received");
                return Response
                                .status(Response.Status.OK)
                                .entity("{\"status\": \"success\", \"message\": \"Test endpoint is operational\"}")
                                .build();
        }

        /**
         * Test endpoint for email functionality.
         * 
         * @param emailTest Contains details about the email test to be performed
         * @return HTTP response indicating success or failure
         */
        @POST
        @Path("/email")
        public Response testEmail(EmailTestDTO emailTest) {
                logger.info("Email test requested for recipient: {}", emailTest.getRecipient());

                if (emailTest.getRecipient() == null || emailTest.getRecipient().isEmpty()) {
                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity("{\"status\": \"error\", \"message\": \"Recipient email is required\"}")
                                        .build();
                }

                if (!emailUtil.isValidEmail(emailTest.getRecipient())) {
                        return Response
                                        .status(Response.Status.BAD_REQUEST)
                                        .entity("{\"status\": \"error\", \"message\": \"Invalid email format\"}")
                                        .build();
                }

                boolean emailSent = false;
                String testType = emailTest.getTestType() != null ? emailTest.getTestType().toLowerCase() : "simple";

                try {
                        switch (testType) {
                                case "confirmation":
                                        String confirmationToken = jwtUtil.generateConfirmationToken(
                                                        emailTest.getRecipient(),
                                                        "127.0.0.1", // Sample IP address
                                                        null // Use default expiration
                                        );

                                        String baseUrl = emailTest.getBaseUrl() != null ? emailTest.getBaseUrl()
                                                        : "http://localhost:8080";
                                        emailSent = emailUtil.sendConfirmationEmail(
                                                        emailTest.getRecipient(),
                                                        confirmationToken,
                                                        baseUrl);

                                        logger.info("Sent confirmation email with token: {}", confirmationToken);
                                        break;

                                case "reset":
                                        String resetToken = jwtUtil.generatePasswordResetToken(
                                                        emailTest.getRecipient(),
                                                        "127.0.0.1", // Sample IP address
                                                        null // Use default expiration
                                        );

                                        baseUrl = emailTest.getBaseUrl() != null ? emailTest.getBaseUrl()
                                                        : "http://localhost:8080";
                                        emailSent = emailUtil.sendPasswordResetEmail(
                                                        emailTest.getRecipient(),
                                                        resetToken,
                                                        baseUrl);

                                        logger.info("Sent password reset email with token: {}", resetToken);
                                        break;

                                default: // "simple" or any other value
                                        String subject = emailTest.getSubject() != null ? emailTest.getSubject()
                                                        : "Test Email";
                                        String message = emailTest.getMessage() != null ? emailTest.getMessage()
                                                        : "This is a test email from the application.";

                                        // Create an HTML body using the template
                                        String body = emailUtil.createEmailTemplate(
                                                        "Email Test",
                                                        message,
                                                        "Visit Website",
                                                        "http://localhost:8080",
                                                        "This is an automatically generated test email.");

                                        emailSent = emailUtil.sendEmail(emailTest.getRecipient(), subject, body);
                                        logger.info("Sent simple test email with subject: {}", subject);
                        }

                        if (emailSent) {
                                return Response
                                                .status(Response.Status.OK)
                                                .entity("{\"status\": \"success\", \"message\": \"Email sent successfully\", \"type\": \""
                                                                + testType + "\"}")
                                                .build();
                        } else {
                                return Response
                                                .status(Response.Status.INTERNAL_SERVER_ERROR)
                                                .entity("{\"status\": \"error\", \"message\": \"Failed to send email\"}")
                                                .build();
                        }
                } catch (Exception e) {
                        logger.error("Error sending test email", e);
                        return Response
                                        .status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity("{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}")
                                        .build();
                }
        }
}
