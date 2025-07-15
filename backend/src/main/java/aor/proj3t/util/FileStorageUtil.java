package aor.proj3t.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.config.AppConstants;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Utility class for managing file storage, particularly user profile pictures.
 * <p>
 * Handles file saving, retrieval, deletion, and image resizing with validation.
 * Files are stored in a server-side directory, with configurable max file size,
 * allowed image types, and fallback behavior for missing images.
 * Also includes automatic directory initialization on server startup.
 * </p>
 *
 * Features:
 * - Stores profile pictures securely with unique timestamps
 * - Resizes images to 300x300 while maintaining aspect ratio
 * - Provides default image fallback on retrieval errors
 * - Prevents deletion of the default profile image
 * - Validates MIME type, size, and format before saving
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class FileStorageUtil {

    private static final Logger logger = LogManager.getLogger(FileStorageUtil.class);

    private Path fileStoragePath;
    private final String defaultProfilePicture = AppConstants.FileStorage.DEFAULT_PROFILE_PICTURE;
    private final String defaultProfilePictureDir = AppConstants.FileStorage.DEFAULT_PROFILE_PICTURE_DIR;

    /**
     * Initializes the file storage directory and checks for default profile image.
     */
    @PostConstruct
    public void init() {
        try {
            String uploadDir = System.getProperty("jboss.server.data.dir") + File.separator + defaultProfilePictureDir;
            this.fileStoragePath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(fileStoragePath);
            logger.info("File storage path initialized: {}", fileStoragePath);
            ensureDefaultProfilePictureExists();
        } catch (IOException e) {
            logger.error("Error initializing file storage path", e);
        }
    }

    /**
     * Verifies the presence of the default profile picture at startup.
     */
    private void ensureDefaultProfilePictureExists() {
        try {
            Path defaultPicturePath = fileStoragePath.resolve(defaultProfilePicture);
            if (!Files.exists(defaultPicturePath)) {
                logger.warn("Default profile picture not found at {}. Please ensure the file is manually placed there.",
                        defaultPicturePath);
            } else {
                logger.info("Default profile picture found at {}", defaultPicturePath);
            }
        } catch (Exception e) {
            logger.error("Error checking default profile picture", e);
        }
    }

    /**
     * Stores a file to disk after validating size and MIME type.
     *
     * @param fileContent raw file bytes
     * @param fileName original file name
     * @param fileExtension file type extension
     * @param mimeType MIME type of the file
     * @return the stored filename
     */
    public String storeFile(byte[] fileContent, String fileName, String fileExtension, String mimeType) {
        try {
            if (fileContent == null || fileContent.length == 0) {
                logger.warn("Empty file content for file: {}", fileName);
                throw new IllegalArgumentException("File content cannot be empty");
            }

            if (fileContent.length > AppConstants.FileStorage.MAX_FILE_SIZE) {
                logger.warn("File size exceeds limit for file: {}", fileName);
                throw new IllegalArgumentException("File size exceeds maximum allowed (5MB)");
            }

            if (!AppConstants.FileStorage.ALLOWED_IMAGE_TYPES.contains(mimeType)) {
                logger.warn("Invalid file type: {} for file: {}", mimeType, fileName);
                throw new IllegalArgumentException("File type not allowed. Only JPEG, PNG and GIF are supported");
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String uniqueId = UUID.randomUUID().toString();
            String versionedFileName = String.format("%s_%s.%s", uniqueId, timestamp, fileExtension);

            Path targetLocation = fileStoragePath.resolve(versionedFileName);
            Files.write(targetLocation, fileContent);
            logger.info("Stored file: {} as {}", fileName, versionedFileName);

            return versionedFileName;
        } catch (IOException e) {
            logger.error("Error storing file: {}", fileName, e);
            throw new RuntimeException("Could not store file " + fileName, e);
        }
    }

    /**
     * Retrieves a stored image by filename.
     * If the file does not exist, returns the default image.
     *
     * @param fileName the name of the file to retrieve
     * @return image byte data
     */
    public byte[] getFile(String fileName) {
        try {
            Path filePath = fileStoragePath.resolve(fileName).normalize();

            if (!filePath.toFile().exists()) {
                logger.warn("File not found: {}, using default profile picture", fileName);
                filePath = fileStoragePath.resolve(defaultProfilePicture).normalize();
            }
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            logger.error("Error retrieving file: {}", fileName, e);
            throw new RuntimeException("Could not retrieve file " + fileName, e);
        }
    }

    /**
     * Deletes a stored file unless it's the default profile image.
     *
     * @param fileName the file to delete
     * @return true if deleted, false otherwise
     */
    public Boolean deleteFile(String fileName) {
        try {
            if (fileName.equals(defaultProfilePicture)) {
                return false; // Prevent deletion of default picture
            }
            Path filePath = fileStoragePath.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            logger.error("Error deleting file: {}", fileName, e);
            return false;
        }
    }

    /**
     * Retrieves the filename of the default profile picture.
     *
     * @return the default profile image filename
     */
    public String getDefaultProfilePicture() {
        return defaultProfilePicture;
    }

    /**
     * Stores a resized profile picture after formatting and dimension scaling.
     *
     * @param fileContent image byte data
     * @param fileName original filename
     * @param fileExtension image extension
     * @param mimeType MIME type
     * @return stored filename
     */
    public String storeProfilePicture(byte[] fileContent, String fileName, String fileExtension, String mimeType) {
        try {
            logger.info("Resizing profile picture: {} ({} bytes)", fileName, fileContent.length);
            byte[] resizedContent = resizeImage(fileContent, 300, 300);
            int originalSize = fileContent.length;
            int newSize = resizedContent.length;
            double reductionPercent = ((originalSize - newSize) / (double) originalSize) * 100.0;
            logger.info("Image resized: {} bytes to {} bytes (reduced by {:.2f}%)",
                    originalSize, newSize, reductionPercent);
            return storeFile(resizedContent, fileName, fileExtension, mimeType);
        } catch (Exception e) {
            logger.error("Error processing profile picture: {}", fileName, e);
            throw new RuntimeException("Could not process profile picture " + fileName, e);
        }
    }

    /**
     * Resizes an image to target dimensions while preserving aspect ratio.
     * Applies high quality rendering and centers the image in the canvas.
     *
     * @param imageData original image byte array
     * @param targetWidth desired width
     * @param targetHeight desired height
     * @return resized image as byte array
     */
    private byte[] resizeImage(byte[] imageData, int targetWidth, int targetHeight) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                logger.warn("Could not read image for resizing, returning original");
                return imageData;
            }

            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            double widthScale = (double) targetWidth / originalWidth;
            double heightScale = (double) targetHeight / originalHeight;
            double scale = Math.max(widthScale, heightScale);
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resized.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, targetWidth, targetHeight);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int x = (targetWidth - scaledWidth) / 2;
            int y = (targetHeight - scaledHeight) / 2;
            g.drawImage(image, x, y, scaledWidth, scaledHeight, null);
            g.dispose();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(resized, "png", bos);
            return bos.toByteArray();
        } catch (IOException e) {
            logger.error("Error resizing image", e);
            return imageData;
        }
    }
}

