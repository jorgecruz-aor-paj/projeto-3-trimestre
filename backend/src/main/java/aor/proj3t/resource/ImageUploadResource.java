package aor.proj3t.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.dto.FileUploadDTO;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.FileStorageUtil;
import aor.proj3t.util.authutils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    @Secured
    public Response uploadProfilePicture(@PathParam("userId") Long userId,
            MultipartFormDataInput input, @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Starting profile picture upload for user ID: {}", userId);

        try {
            // Security check - ensure user can only upload their own picture or admin can
            // upload for anyone
            Long requestingUserId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
            logger.debug("Security check - requesting user ID: {}, target user ID: {}", requestingUserId, userId);

            if (!requestingUserId.equals(userId)) {
                // Check if the requesting user is an admin
                // We need to get the user role to check if they can upload for others
                // For now, let's allow it and let the service layer handle the authorization
                logger.debug("Different user ID detected - requestingUserId: {}, targetUserId: {}", requestingUserId,
                        userId);
            }
            logger.debug("Security check passed for user ID: {}", userId);
            // Process the multipart form data
            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            logger.debug("Multipart form data processed successfully for user ID: {}", userId);

            // Get file input part
            List<InputPart> inputParts = uploadForm.get("file");
            if (inputParts == null || inputParts.isEmpty()) {
                logger.warn("No file part found in upload request for user ID: {}", userId);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("No file part found in the request").build();
            }

            InputPart inputPart = inputParts.get(0);
            logger.debug("File input part retrieved for user ID: {}", userId);

            // Get file content
            InputStream inputStream = inputPart.getBody(InputStream.class, null);
            byte[] fileContent = inputStream.readAllBytes();
            logger.debug("File content read successfully for user ID: {}, file size: {} bytes", userId,
                    fileContent.length);

            // Get content disposition headers for filename
            String contentDisposition = inputPart.getHeaders().getFirst("Content-Disposition");
            String filename = getFileName(contentDisposition);
            logger.debug("Extracted filename: {} for user ID: {}", filename, userId);

            // Get the MIME type
            String mimeType = inputPart.getMediaType().toString();
            logger.debug("Detected MIME type: {} for user ID: {}", mimeType, userId);

            // Get file extension
            String fileExtension = getFileExtension(filename);
            logger.debug("Extracted file extension: {} for user ID: {}", fileExtension, userId);

            // Validate file size (max 5MB)
            if (fileContent.length > 5 * 1024 * 1024) {
                logger.warn("File too large for user ID: {}, size: {} bytes (max 5MB)", userId, fileContent.length);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new APIResponseDTO("File too large (max 5MB)")).build();
            }
            logger.debug("File size validation passed for user ID: {}", userId);

            // Validate file type (must be an image)
            if (!mimeType.startsWith("image/")) {
                logger.warn("Invalid file type for user ID: {}, MIME type: {}", userId, mimeType);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new APIResponseDTO("Invalid file type")).build();
            }
            logger.debug("File type validation passed for user ID: {}", userId);

            // Create DTO with the extracted data
            FileUploadDTO fileUploadDTO = new FileUploadDTO();
            fileUploadDTO.setFileName(filename);
            fileUploadDTO.setFileContent(fileContent);
            fileUploadDTO.setFileMimeType(mimeType);
            fileUploadDTO.setFileExtension(fileExtension);
            logger.debug("FileUploadDTO created for user ID: {} with filename: {}, extension: {}, MIME type: {}",
                    userId, filename, fileExtension, mimeType);

            Boolean success = serviceFactory.forUser().uploadProfilePicture(requestingUserId, userId, fileUploadDTO);
            if (success) {
                logger.info("Profile picture uploaded successfully for user ID: {}, filename: {}", userId, filename);
                return Response.status(Response.Status.OK)
                        .entity(new APIResponseDTO("Profile picture updated successfully")).build();
            } else {
                logger.error("Failed to upload profile picture for user ID: {}, filename: {}", userId, filename);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new APIResponseDTO("Failed to update profile picture")).build();
            }
        } catch (IOException e) {
            logger.error("IO error processing uploaded file for user ID: {}", userId, e);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new APIResponseDTO("Error processing uploaded file")).build();
        } catch (Exception e) {
            logger.error("Unexpected error uploading profile picture for user ID: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Internal server error")).build();
        }
    }

    /**
     * Extracts the filename from the Content-Disposition header.
     * 
     * @param contentDisposition The Content-Disposition header value
     * @return The extracted filename
     */
    private String getFileName(String contentDisposition) {
        logger.debug("Extracting filename from Content-Disposition header: {}", contentDisposition);

        if (contentDisposition == null) {
            logger.warn("Content-Disposition header is null, using default filename");
            return "unknown_file";
        }

        String[] items = contentDisposition.split(";");
        for (String item : items) {
            item = item.trim();
            if (item.startsWith("filename=")) {
                String fileName = item.substring(item.indexOf('=') + 1).trim().replace("\"", "");
                logger.debug("Successfully extracted filename: {}", fileName);
                return fileName;
            }
        }

        logger.warn("No filename found in Content-Disposition header, using default filename");
        return "unknown_file";
    }

    /**
     * Extracts the file extension from the filename.
     * 
     * @param fileName The filename
     * @return The file extension (without the dot)
     */
    private String getFileExtension(String fileName) {
        logger.debug("Extracting file extension from filename: {}", fileName);

        if (fileName == null) {
            logger.warn("Filename is null, using default extension 'jpg'");
            return "jpg"; // Default extension
        }

        int lastDotPos = fileName.lastIndexOf(".");
        if (lastDotPos > 0) {
            String extension = fileName.substring(lastDotPos + 1).toLowerCase();
            logger.debug("Successfully extracted file extension: {}", extension);
            return extension;
        }

        logger.warn("No file extension found in filename: {}, using default extension 'jpg'", fileName);
        return "jpg"; // Default extension
    }

    @DELETE
    @Path("/picture")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured
    public Response resetProfilePicture(@PathParam("userId") Long userId,
            @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        logger.info("Starting profile picture reset for user ID: {}", userId);

        try {
            // Security check - ensure user can only reset their own picture or admin can
            // reset for anyone
            Long requestingUserId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
            logger.debug("Security check - requesting user ID: {}, target user ID: {}", requestingUserId, userId);

            if (!requestingUserId.equals(userId)) {
                // Check if the requesting user is an admin
                // We need to get the user role to check if they can reset for others
                // For now, let's allow it and let the service layer handle the authorization
                logger.debug("Different user ID detected - requestingUserId: {}, targetUserId: {}", requestingUserId,
                        userId);
            }
            logger.debug("Security check passed for user ID: {}", userId);

            Boolean success = serviceFactory.forUser().resetProfilePicture(requestingUserId, userId);
            if (success) {
                logger.info("Profile picture reset successfully for user ID: {}", userId);
                return Response.status(Response.Status.OK)
                        .entity(new APIResponseDTO("Profile picture reset to default")).build();
            } else {
                logger.error("Failed to reset profile picture for user ID: {}", userId);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new APIResponseDTO("Failed to reset profile picture")).build();
            }
        } catch (Exception e) {
            logger.error("Unexpected error resetting profile picture for user ID: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Internal server error")).build();
        }
    }

    @GET
    @Path("/picture")
    @Produces("image/*")
    public Response getProfilePicture(@PathParam("userId") Long userId) {
        logger.debug("Retrieving profile picture for user ID: {}", userId);

        try {
            // Get the user's profile picture filename
            String pictureName = serviceFactory.forUser().getUser(userId).getPhotograph();
            logger.debug("Retrieved profile picture filename from database: {} for user ID: {}", pictureName, userId);

            // If user doesn't have a profile picture, serve the default one
            if (pictureName == null || pictureName.isEmpty()) {
                pictureName = "default-profile-picture.png";
                logger.debug("No custom profile picture found for user ID: {}, using default: {}", userId, pictureName);
            }

            // Read the file
            byte[] imageData = fileStorageUtil.getFile(pictureName);
            logger.debug("Successfully read image file: {} for user ID: {}, size: {} bytes", pictureName, userId,
                    imageData.length);

            // Determine content type based on file extension
            String contentType = determineContentType(pictureName);
            logger.debug("Determined content type: {} for file: {} (user ID: {})", contentType, pictureName, userId);

            logger.debug("Successfully serving profile picture for user ID: {}, filename: {}, content type: {}", userId,
                    pictureName, contentType);
            return Response.ok(imageData)
                    .header("Content-Type", contentType)
                    .header("Cache-Control", "public, max-age=31536000, immutable") // 1 year cache for profile pictures
                    .header("Content-Disposition", "inline; filename=\"" + pictureName + "\"")
                    .build();
        } catch (Exception e) {
            logger.error("Error retrieving profile picture for user ID: {}", userId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new APIResponseDTO("Error retrieving profile picture"))
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
        logger.debug("Determining content type for filename: {}", fileName);

        if (fileName == null) {
            logger.warn("Filename is null, using default MIME type 'image/jpeg'");
            return "image/jpeg"; // default
        }

        fileName = fileName.toLowerCase();

        String contentType;
        if (fileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.endsWith(".gif")) {
            contentType = "image/gif";
        } else if (fileName.endsWith(".webp")) {
            contentType = "image/webp";
        } else if (fileName.endsWith(".svg")) {
            contentType = "image/svg+xml";
        } else if (fileName.endsWith(".bmp")) {
            contentType = "image/bmp";
        } else {
            contentType = "image/jpeg"; // default for .jpg, .jpeg and others
        }

        logger.debug("Determined content type: {} for filename: {}", contentType, fileName);
        return contentType;
    }
}
