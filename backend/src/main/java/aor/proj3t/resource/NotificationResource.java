package aor.proj3t.resource;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.dto.NotificationDTO;
import aor.proj3t.dto.paginated.NotificationPaginationDTO;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST endpoint for notification-related operations.
 * This class handles requests for retrieving, marking as read, and managing
 * notifications.
 */
@Path("/notifications")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    private static final Logger logger = LogManager.getLogger(NotificationResource.class);

    @Inject
    ServiceFactory serviceFactory;

    /**
     * Get all notifications for the authenticated user
     * 
     * @param authHeader The authorization header containing the JWT token
     * @return Response containing the list of user notifications
     */
    @GET
    @Path("/user")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserNotifications(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to get user notifications");

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
            List<NotificationDTO> notifications = serviceFactory.forNotification().getUserNotifications(userId);

            logger.info("Retrieved {} notifications for user {}", notifications.size(), userId);
            return Response.ok(notifications).build();

        } catch (Exception e) {
            logger.error("Error retrieving user notifications", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error retrieving notifications: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get notifications for the authenticated user with pagination
     * 
     * @param authHeader The authorization header containing the JWT token
     * @param page       The page number (0-based)
     * @param size       The page size
     * @return Response containing the paginated list of user notifications
     */
    @GET
    @Path("/user/paginated")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserNotificationsPaginated(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("10") @QueryParam("size") int size) {
        logger.info("Received request to get user notifications paginated (page: {}, size: {})", page, size);

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
            NotificationPaginationDTO paginatedNotifications = serviceFactory.forNotification()
                    .getUserNotificationsPaginated(userId, page, size);

            logger.info("Retrieved {} notifications for user {} (page {}, size {})",
                    paginatedNotifications.getEntities().size(), userId, page, size);
            return Response.ok(paginatedNotifications).build();

        } catch (Exception e) {
            logger.error("Error retrieving user notifications paginated", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error retrieving notifications: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Mark a specific notification as read
     * 
     * @param authHeader     The authorization header containing the JWT token
     * @param notificationId The ID of the notification to mark as read
     * @return Response indicating success or failure
     */
    @PUT
    @Path("/{notificationId}/read")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response markNotificationAsRead(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("notificationId") Long notificationId) {
        logger.info("Received request to mark notification {} as read", notificationId);

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Verify that the user is the recipient of this notification
            NotificationDTO notification = serviceFactory.forNotification().getNotification(notificationId);
            if (!notification.getRecipientId().equals(userId)) {
                logger.warn("User {} attempted to mark notification {} as read, but is not the recipient",
                        userId, notificationId);
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(new APIResponseDTO("You can only mark your own notifications as read"))
                        .build();
            }

            Boolean success = serviceFactory.forNotification().markAsRead(notificationId);
            if (success) {
                logger.info("Notification {} marked as read by user {}", notificationId, userId);
                return Response.ok(new APIResponseDTO("Notification marked as read")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new APIResponseDTO("Failed to mark notification as read"))
                        .build();
            }

        } catch (Exception e) {
            logger.error("Error marking notification as read", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error marking notification as read: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Mark all notifications as read for the authenticated user
     * 
     * @param authHeader The authorization header containing the JWT token
     * @return Response indicating success or failure
     */
    @PUT
    @Path("/read-all")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response markAllNotificationsAsRead(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to mark all notifications as read");

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
            Integer count = serviceFactory.forNotification().markAllAsRead(userId);

            logger.info("Marked {} notifications as read for user {}", count, userId);
            return Response.ok(new APIResponseDTO("All notifications marked as read")).build();

        } catch (Exception e) {
            logger.error("Error marking all notifications as read", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error marking all notifications as read: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Delete a notification
     * 
     * @param authHeader     The authorization header containing the JWT token
     * @param notificationId The ID of the notification to delete
     * @return Response indicating success or failure
     */
    @DELETE
    @Path("/{notificationId}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteNotification(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("notificationId") Long notificationId) {
        logger.info("Received request to delete notification {}", notificationId);

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Verify that the user is the recipient of this notification
            NotificationDTO notification = serviceFactory.forNotification().getNotification(notificationId);
            if (!notification.getRecipientId().equals(userId)) {
                logger.warn("User {} attempted to delete notification {} but is not the recipient",
                        userId, notificationId);
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(new APIResponseDTO("You can only delete your own notifications"))
                        .build();
            }

            Boolean success = serviceFactory.forNotification().deleteNotification(notificationId);
            if (success) {
                logger.info("Notification {} deleted by user {}", notificationId, userId);
                return Response.ok(new APIResponseDTO("Notification deleted")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new APIResponseDTO("Failed to delete notification"))
                        .build();
            }

        } catch (Exception e) {
            logger.error("Error deleting notification", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error deleting notification: " + e.getMessage()))
                    .build();
        }
    }
}
