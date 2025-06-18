package aor.proj3t.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "USER", "ADMIN" })
    public Response uploadProfilePicture(@PathParam("userId") Long userId,
            MultipartFormDataInput input) {
        try {
            // Process the multipart form data
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

            // Get file input part
            List<InputPart> inputParts = uploadForm.get("file");
            if (inputParts == null || inputParts.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No file part found in the request").build();
            }

            InputPart inputPart = inputParts.get(0);

            // Get file content
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] fileContent = inputStream.readAllBytes();

            // Get content disposition headers for filename
            String contentDisposition = inputPart.getHeaders().getFirst("Content-Disposition");
            String filename = getFileName(contentDisposition);

            // Get the MIME type
            String mimeType = inputPart.getMediaType().toString();

            // Get file extension
            String fileExtension = getFileExtension(filename);

            // Create DTO with the extracted data
            FileUploadDTO fileUploadDTO = new FileUploadDTO();
            fileUploadDTO.setFileName(filename);
            fileUploadDTO.setFileContent(fileContent);
            fileUploadDTO.setFileMimeType(mimeType);
            fileUploadDTO.setFileExtension(fileExtension);

            Boolean success = serviceFactory.forUser().uploadProfilePicture(userId, fileUploadDTO);
            if (success) {
                return Response.status(Response.Status.OK).entity(new MessageResponse("Profile picture updated successfully")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(new MessageResponse("Failed to update profile picture")).build();
            }
        } catch (IOException e) {
            logger.error("Error processing uploaded file for user ID: {}", userId, e);
            return Response.status(Response.Status.BAD_REQUEST).entity(new MessageResponse("Error processing uploaded file")).build();
        } catch (Exception e) {
            logger.error("Error uploading profile picture for user ID: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MessageResponse("Internal server error")).build();
        }
    }

    /**
     * Extracts the filename from the Content-Disposition header.
     * 
     * @param contentDisposition The Content-Disposition header value
     * @return The extracted filename
     */
    private String getFileName(String contentDisposition) {
        if (contentDisposition == null) {
            return "unknown_file";
        }

        String[] items = contentDisposition.split(";");
        for (String item : items) {
            item = item.trim();
            if (item.startsWith("filename=")) {
                String fileName = item.substring(item.indexOf('=') + 1).trim().replace("\"", "");
                return fileName;
            }
        }
        return "unknown_file";
    }

    /**
     * Extracts the file extension from the filename.
     * 
     * @param fileName The filename
     * @return The file extension (without the dot)
     */
    private String getFileExtension(String fileName) {
        if (fileName == null) {
            return "jpg"; // Default extension
        }

        int lastDotPos = fileName.lastIndexOf(".");
        if (lastDotPos > 0) {
            return fileName.substring(lastDotPos + 1).toLowerCase();
        }

        return "jpg"; // Default extension
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
                        .entity(new MessageResponse("Not authorized to reset this user's profile picture")).build();
            }

            Boolean success = serviceFactory.forUser().resetProfilePicture(userId);
            if (success) {
                return Response.status(Response.Status.OK).entity(new MessageResponse("Profile picture reset to default")).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity(new MessageResponse("Failed to reset profile picture")).build();
            }
        } catch (Exception e) {
            logger.error("Error resetting profile picture for user ID: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MessageResponse("Internal server error")).build();
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
            String contentType = determineContentType(pictureName);
            return Response.ok(imageData)
                    .header("Content-Type", contentType)
                    .header("Content-Disposition", "inline; filename=\"" + pictureName + "\"")
                    .build();
        } catch (Exception e) {
            logger.error("Error retrieving profile picture for user ID: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new MessageResponse("Error retrieving profile picture"))
                    .build();
        }
    }

    /**
     * Determines the content type based on the file extension.
     * 
     * @param fileName The filename to analyze
     * @return The corresponding MIME type
     */
    private String determineContentType(String fileName) {
        if (fileName == null) {
            return "image/jpeg"; // default
        }

        fileName = fileName.toLowerCase();

        if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".webp")) {
            return "image/webp";
        } else if (fileName.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (fileName.endsWith(".bmp")) {
            return "image/bmp";
        } else {
            return "image/jpeg"; // default for .jpg, .jpeg and others
        }
    }
}
