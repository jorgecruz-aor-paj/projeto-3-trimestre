package aor.proj3t.resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.FileUploadDTO;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.FileStorageUtil;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/users/{userId}/profile")
@RequestScoped
public class ImageUploadResource {

    private static final Logger logger = LogManager.getLogger(ImageUploadResource.class);

    @Inject
    private ServiceFactory serviceFactory;

    @Inject
    private FileStorageUtil fileStorageUtil;

    @POST
    @Path("/picture")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "USER", "ADMIN" })
    public Response uploadProfilePicture(@PathParam("userId") Long userId, FileUploadDTO fileUploadDTO,
            @Context SecurityContext securityContext) {
        try {
            // Security check - only the user themselves or an admin can upload their
            // profile picture
            String userIdStr = securityContext.getUserPrincipal().getName();
            if (!userIdStr.equals(userId.toString()) && !securityContext.isUserInRole("ADMIN")) {
                return Response.status(Response.Status.FORBIDDEN).entity("Not authorized to update this user's profile")
                        .build();
            }

            Boolean success = serviceFactory.forUser().uploadProfilePicture(userId, fileUploadDTO);
            if (success) {
                return Response.status(Response.Status.OK).entity("Profile picture updated successfully").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Failed to update profile picture").build();
            }
        } catch (Exception e) {
            logger.error("Error uploading profile picture for user ID: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal server error").build();
        }
    }

    @DELETE
    @Path("/picture")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "USER", "ADMIN" })
    public Response resetProfilePicture(@PathParam("userId") Long userId, @Context SecurityContext securityContext) {
        try {
            // Security check
            String userIdStr = securityContext.getUserPrincipal().getName();
            if (!userIdStr.equals(userId.toString()) && !securityContext.isUserInRole("ADMIN")) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("Not authorized to reset this user's profile picture").build();
            }

            Boolean success = serviceFactory.forUser().resetProfilePicture(userId);
            if (success) {
                return Response.status(Response.Status.OK).entity("Profile picture reset to default").build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("Failed to reset profile picture").build();
            }
        } catch (Exception e) {
            logger.error("Error resetting profile picture for user ID: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal server error").build();
        }
    }

    @GET
    @Path("/picture")
    @Produces("image/*")
    public Response getProfilePicture(@PathParam("userId") Long userId) {
        try {
            // Get the user's profile picture filename
            String pictureName = serviceFactory.forUser().getUser(userId).getPhotograph();

            // Read the file
            byte[] imageData = fileStorageUtil.getFile(pictureName);

            // Determine content type based on file extension
            String contentType = "image/jpeg"; // default
            if (pictureName.endsWith(".png")) {
                contentType = "image/png";
            } else if (pictureName.endsWith(".gif")) {
                contentType = "image/gif";
            }

            return Response.ok(imageData)
                    .header("Content-Type", contentType)
                    .header("Content-Disposition", "inline; filename=\"" + pictureName + "\"")
                    .build();
        } catch (Exception e) {
            logger.error("Error retrieving profile picture for user ID: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving profile picture")
                    .build();
        }
    }

}
