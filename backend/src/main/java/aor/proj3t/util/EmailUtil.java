package aor.proj3t.util;

import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.AppConstants;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Utility class for sending emails and email validation.
 * 
 * This class provides methods for sending emails, including account validation
 * emails, and validating email formats.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 */
@ApplicationScoped
public class EmailUtil {

    private static final Logger logger = LogManager.getLogger(EmailUtil.class);

    @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;

    // Email configuration from environment variables
    private String smtpHost;
    private String smtpPort;
    private String smtpUsername;
    private String smtpPassword;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    @PostConstruct
    public void init() {
        logger.info("Initializing Email utility service");
        try {
            // Try to get from environment variables first
            smtpHost = System.getenv("SMTP_HOST");
            smtpPort = System.getenv("SMTP_PORT");
            smtpUsername = System.getenv("SMTP_USERNAME");
            smtpPassword = System.getenv("SMTP_PASSWORD");

            // If not found, try Dotenv (.env file)
            if ((smtpHost == null || smtpHost.isEmpty()) ||
                (smtpPort == null || smtpPort.isEmpty()) ||
                (smtpUsername == null || smtpUsername.isEmpty()) ||
                (smtpPassword == null || smtpPassword.isEmpty())) {
                logger.debug("Some SMTP environment variables not set, trying to load from .env file");
                Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().load();
                if (smtpHost == null || smtpHost.isEmpty()) {
                    smtpHost = dotenv.get("SMTP_HOST");
                }
                if (smtpPort == null || smtpPort.isEmpty()) {
                    smtpPort = dotenv.get("SMTP_PORT");
                }
                if (smtpUsername == null || smtpUsername.isEmpty()) {
                    smtpUsername = dotenv.get("SMTP_USERNAME");
                }
                if (smtpPassword == null || smtpPassword.isEmpty()) {
                    smtpPassword = dotenv.get("SMTP_PASSWORD");
                }
            }

            if (smtpHost == null || smtpHost.isEmpty()) {
                logger.warn("SMTP_HOST is not set in environment variables or .env file");
            }
            if (smtpPort == null || smtpPort.isEmpty()) {
                logger.warn("SMTP_PORT is not set in environment variables or .env file");
            }
            if (smtpUsername == null || smtpUsername.isEmpty()) {
                logger.warn("SMTP_USERNAME is not set in environment variables or .env file");
            }
            if (smtpPassword == null || smtpPassword.isEmpty()) {
                logger.warn("SMTP_PASSWORD is not set in environment variables or .env file");
            }

            logger.info("Email utility service initialized successfully");
        } catch (DotenvException e) {
            logger.error("Failed to load email configuration from environment variables or .env file", e);
        } catch (Exception e) {
            logger.error("Unexpected error during email utility initialization", e);
        }
    }

    /**
     * Validates if an email address follows the standard format.
     * 
     * @param email The email address to validate
     * @return true if the email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Creates a session object for sending emails.
     * 
     * @return the configured Session object
     */
    private Session createSession() {
        // Check if we can use injected mailSession (configured in Wildfly)
        if (mailSession != null) {
            logger.debug("Using injected mail session from server configuration");
            return mailSession;
        }

        // Fall back to creating a session from environment variables
        logger.debug("Creating mail session from environment variables");
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        });
    }

    /**
     * Sends an email with the specified parameters.
     * 
     * @param toEmail the recipient's email address
     * @param subject the email subject
     * @param body    the email body content (HTML is supported)
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendEmail(String toEmail, String subject, String body) {
        try {
            if (!isValidEmail(toEmail)) {
                logger.warn("Attempted to send email to invalid address: {}", toEmail);
                return false;
            }

            Session session = createSession();
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(smtpUsername));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);

            logger.info("Email sent successfully to {}", toEmail);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}", toEmail, e);
            return false;
        }
    }

    /**
     * Sends an account confirmation email with a validation link.
     * 
     * @param toEmail the recipient's email address
     * @param token   the confirmation token
     * @param baseUrl the application base URL (e.g., "https://yourapp.com")
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendConfirmationEmail(String toEmail, String token, String baseUrl) {
        String subject = AppConstants.Email.DEFAULT_CONFIRMATION_SUBJECT;
        String confirmationLink = baseUrl + "/register/confirm?confirmationToken=" + token;
        logger.info("Generated confirmation link: {}", confirmationLink);

        String body = createEmailTemplate(
                "Account Confirmation",
                "Thank you for registering with us! To complete your registration and activate your account, please click the button below to confirm your email address.",
                "Confirm Account",
                confirmationLink,
                "If you didn't create an account with us, you can safely ignore this email. This confirmation link will expire in "
                        + AppConstants.Jwt.DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME
                        + " minutes for security reasons.");

        return sendEmail(toEmail, subject, body);
    }

    /**
     * Sends a password reset email with a reset link.
     * 
     * @param toEmail the recipient's email address
     * @param token   the reset token
     * @param baseUrl the application base URL (e.g., "https://yourapp.com")
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendPasswordResetEmail(String toEmail, String token, String baseUrl) {
        String subject = AppConstants.Email.DEFAULT_PASSWORD_RESET_SUBJECT;
        String resetLink = baseUrl + "/reset-password/confirm?resetPasswordToken=" + token;
        logger.info("Generated reset password link: {}", resetLink);

        String body = createEmailTemplate(
                "Password Reset Request",
                "We received a request to reset your password. If you made this request, please click the button below to create a new password for your account.",
                "Reset Password",
                resetLink,
                "If you didn't request a password reset, please ignore this email or contact our support team if you're concerned about your account security. This reset link will expire in "
                        + AppConstants.Email.DEFAULT_PASSWORD_RESET_EXPIRATION_MINUTES
                        + " minutes for security reasons.");

        return sendEmail(toEmail, subject, body);
    }

    /**
     * Creates a customized HTML email template.
     * 
     * @param title      the main title of the email
     * @param message    the main message body
     * @param buttonText the text for the call-to-action button
     * @param buttonUrl  the URL for the call-to-action button
     * @param footerText additional text for the email footer
     * @return HTML formatted email body
     */
    public String createEmailTemplate(String title, String message, String buttonText, String buttonUrl,
            String footerText) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f8f9fa; }"
                +
                ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                ".header { background-color: #C01722; color: white; padding: 30px 20px; text-align: center; border-radius: 8px 8px 0 0; }"
                +
                ".header h1 { margin: 0; font-size: 28px; font-weight: bold; }" +
                ".content { padding: 30px 20px; background-color: #ffffff; border-radius: 0 0 8px 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }"
                +
                ".content p { margin: 0 0 20px 0; font-size: 16px; }" +
                ".button { display: inline-block; padding: 15px 30px; background-color: #C01722; " +
                "color: white !important; text-decoration: none; border-radius: 5px; margin: 20px 0; font-weight: bold; "
                +
                "transition: background-color 0.3s ease; }" +
                ".button:hover { background-color: #A01219; color: white !important; text-decoration: none; }" +
                ".footer { font-size: 14px; color: #6c757d; padding-top: 20px; text-align: center; line-height: 1.5; }"
                +
                ".divider { height: 1px; background-color: #e9ecef; margin: 20px 0; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'><h1>" + title + "</h1></div>" +
                "<div class='content'>" +
                "<p>" + message + "</p>" +
                "<div class='divider'></div>" +
                "<center><a href='" + buttonUrl + "' class='button'>" + buttonText + "</a></center>" +
                "<div class='divider'></div>" +
                "<div class='footer'>" + footerText + "</div>" +
                "</div></div>" +
                "</body></html>";
    }
}
