package aor.proj3t.resource;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.dto.MessageDTO;
import aor.proj3t.dto.UserDTO;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import aor.proj3t.websocket.AppWebSocket;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST endpoint for message-related operations.
 * This class handles requests for sending, receiving and managing messages
 * between users.
 */
@Path("/messages")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {

    private static final Logger logger = LogManager.getLogger(MessageResource.class);

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    DAOFactory daoFactory;

    @Inject
    AppWebSocket webSocket;

    /**
     * Send a new message to another user
     * 
     * @param authHeader  The authorization header containing the JWT token
     * @param recipientId The ID of the message recipient
     * @param content     The message content (as plain text)
     * @return Response containing the created message
     */
    @POST
    @Path("/send/{recipientId}")
    @Secured
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendMessage(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("recipientId") Long recipientId,
            String content) {
        logger.info("Received request to send message to user {}", recipientId);

        if (content == null || content.trim().isEmpty()) {
            logger.warn("Empty message content received");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new APIResponseDTO("Message content cannot be empty"))
                    .build();
        }

        try {
            Long senderId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
            logger.info("Sending message from user {} to user {}", senderId, recipientId);

            // Create message entity
            aor.proj3t.entities.Message message = new aor.proj3t.entities.Message();
            message.setContent(content.trim());

            // Get User entities for setting relationships
            aor.proj3t.entities.User sender = daoFactory.forUser().findById(senderId)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));
            aor.proj3t.entities.User recipient = daoFactory.forUser().findById(recipientId)
                    .orElseThrow(() -> new RuntimeException("Recipient not found"));

            message.setSender(sender);
            message.setRecipient(recipient);

            // Save message
            aor.proj3t.entities.Message savedMessage = serviceFactory.forMessage().createMessage(message);
            MessageDTO messageDTO = serviceFactory.forMessage().getMessage(savedMessage.getId());

            // Send simple notification via WebSocket (no message content)
            webSocket.notifyNewMessage(recipientId, savedMessage.getId());

            logger.info("Message successfully created with ID: {}", savedMessage.getId());
            return Response.status(Response.Status.CREATED).entity(messageDTO).build();

        } catch (Exception e) {
            logger.error("Error sending message", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error sending message: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get messages between the authenticated user and another user (conversation)
     * 
     * @param authHeader  The authorization header containing the JWT token
     * @param otherUserId The ID of the other user in the conversation
     * @param page        Page number for pagination (default: 0)
     * @param pageSize    Number of messages per page (default: 50)
     * @return Response containing the list of messages
     */
    @GET
    @Path("/conversation/{otherUserId}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConversation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("otherUserId") Long otherUserId,
            @QueryParam("page") int page,
            @QueryParam("pageSize") int pageSize) {
        logger.info("Received request to get conversation with user {}", otherUserId);

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Set default pagination values
            if (pageSize <= 0)
                pageSize = 50;
            if (page < 0)
                page = 0;

            List<MessageDTO> messages = serviceFactory.forMessage().getMessagesBetweenUsers(userId, otherUserId);

            // Simple pagination (in a real app, you'd implement this in the DAO layer)
            int startIndex = page * pageSize;
            int endIndex = Math.min(startIndex + pageSize, messages.size());

            if (startIndex < messages.size()) {
                messages = messages.subList(startIndex, endIndex);
            } else {
                messages = List.of(); // Empty list if page is beyond available data
            }

            logger.info("Retrieved {} messages for conversation between users {} and {}",
                    messages.size(), userId, otherUserId);
            return Response.ok(messages).build();

        } catch (Exception e) {
            logger.error("Error retrieving conversation", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error retrieving conversation: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get all users with whom the authenticated user has exchanged messages
     * 
     * @param authHeader The authorization header containing the JWT token
     * @return Response containing the list of conversation partners
     */
    @GET
    @Path("/conversations")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConversations(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to get conversation partners");

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Get users the current user has messaged with
            List<aor.proj3t.entities.User> partners = serviceFactory.forMessage().getMessagedUsers(userId);

            // Convert to DTOs
            List<UserDTO> partnerDTOs = partners.stream()
                    .map(user -> serviceFactory.forUser().getUser(user.getId()))
                    .toList();

            logger.info("Retrieved {} conversation partners for user {}", partnerDTOs.size(), userId);
            return Response.ok(partnerDTOs).build();

        } catch (Exception e) {
            logger.error("Error retrieving conversation partners", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error retrieving conversations: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get the count of unread messages for the authenticated user
     * 
     * @param authHeader The authorization header containing the JWT token
     * @return Response containing the unread message count
     */
    @GET
    @Path("/unread/count")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnreadMessageCount(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to get unread message count");

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
            Integer count = serviceFactory.forMessage().getUnreadMessageCount(userId);

            logger.info("User {} has {} unread messages", userId, count);
            return Response.ok(java.util.Map.of("count", count)).build();

        } catch (Exception e) {
            logger.error("Error retrieving unread message count", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error retrieving unread count: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get the count of unread messages from a specific user
     * 
     * @param authHeader The authorization header containing the JWT token
     * @param senderId   The ID of the sender
     * @return Response containing the unread message count from that sender
     */
    @GET
    @Path("/unread/count/{senderId}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnreadMessageCountFromSender(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("senderId") Long senderId) {
        logger.info("Received request to get unread message count from sender {}", senderId);

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
            Integer count = serviceFactory.forMessage().getUnreadMessageCountBetweenUsers(userId, senderId);

            logger.info("User {} has {} unread messages from sender {}", userId, count, senderId);
            return Response.ok(java.util.Map.of("count", count)).build();

        } catch (Exception e) {
            logger.error("Error retrieving unread message count from sender {}", senderId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error retrieving unread count: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Mark a specific message as read
     * 
     * @param authHeader The authorization header containing the JWT token
     * @param messageId  The ID of the message to mark as read
     * @return Response indicating success or failure
     */
    @PUT
    @Path("/{messageId}/read")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response markMessageAsRead(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("messageId") Long messageId) {
        logger.info("Received request to mark message {} as read", messageId);

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);

            // Verify that the user is the recipient of this message
            MessageDTO message = serviceFactory.forMessage().getMessage(messageId);
            if (!message.getRecipientId().equals(userId)) {
                logger.warn("User {} attempted to mark message {} as read, but is not the recipient",
                        userId, messageId);
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(new APIResponseDTO("You can only mark your own messages as read"))
                        .build();
            }

            Boolean success = serviceFactory.forMessage().markAsRead(messageId);
            if (success) {
                logger.info("Message {} marked as read by user {}", messageId, userId);
                return Response.ok(new APIResponseDTO("Message marked as read")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new APIResponseDTO("Failed to mark message as read"))
                        .build();
            }

        } catch (Exception e) {
            logger.error("Error marking message as read", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error marking message as read: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * Get all messages for the authenticated user (sent and received)
     * 
     * @param authHeader The authorization header containing the JWT token
     * @return Response containing all user messages
     */
    @GET
    @Path("/all")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUserMessages(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Received request to get all user messages");

        try {
            Long userId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
            List<MessageDTO> messages = serviceFactory.forMessage().getUserMessages(userId);

            logger.info("Retrieved {} messages for user {}", messages.size(), userId);
            return Response.ok(messages).build();

        } catch (Exception e) {
            logger.error("Error retrieving user messages", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error retrieving messages: " + e.getMessage()))
                    .build();
        }
    }
}
