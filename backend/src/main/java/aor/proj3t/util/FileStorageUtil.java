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

@ApplicationScoped
public class FileStorageUtil {

    private static final Logger logger = LogManager.getLogger(FileStorageUtil.class);

    private Path fileStoragePath;
    private final String defaultProfilePicture = AppConstants.FileStorage.DEFAULT_PROFILE_PICTURE;
    private final String defaultProfilePictureDir =
            AppConstants.FileStorage.DEFAULT_PROFILE_PICTURE_DIR;

    @PostConstruct
    public void init() {
        try {
            String uploadDir = System.getProperty("jboss.server.data.dir") + File.separator
                    + defaultProfilePictureDir;
            this.fileStoragePath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(fileStoragePath);
            logger.info("File storage path initialized: {}", fileStoragePath);
            ensureDefaultProfilePictureExists();
        } catch (IOException e) {
            logger.error("Error initializing file storage path", e);
        }
    }

    private void ensureDefaultProfilePictureExists() {
        try {
            Path defaultPicturePath = fileStoragePath.resolve(defaultProfilePicture);
            if (!Files.exists(defaultPicturePath)) {
                logger.warn(
                        "Default profile picture not found at {}. Please ensure the file is manually placed there.",
                        defaultPicturePath);
            } else {
                logger.info("Default profile picture found at {}", defaultPicturePath);
            }
        } catch (Exception e) {
            logger.error("Error checking default profile picture", e);
        }
    }

    public String storeFile(byte[] fileContent, String fileName, String fileExtension,
            String mimeType) {
        try {
            if (fileContent == null || fileContent.length == 0) {
                logger.warn("Empty file content for file: {}", fileName);
                throw new IllegalArgumentException("File content cannot be empty");
            }

            if (fileContent.length > AppConstants.FileStorage.MAX_FILE_SIZE) {
                logger.warn("File size exceeds limit for file: {}", fileName);
                throw new IllegalArgumentException(
                        "File size exceeds maximum allowed (5MB)");
            }

            if (!AppConstants.FileStorage.ALLOWED_IMAGE_TYPES.contains(mimeType)) {
                logger.warn("Invalid file type: {} for file: {}", mimeType, fileName);
                throw new IllegalArgumentException(
                        "File type not allowed. Only JPEG, PNG and GIF are supported");
            }

            String timestamp =
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String uniqueId = UUID.randomUUID().toString();
            String versionedFileName =
                    String.format("%s_%s.%s", uniqueId, timestamp, fileExtension);

            Path targetLocation = fileStoragePath.resolve(versionedFileName);
            Files.write(targetLocation, fileContent);
            logger.info("Stored file: {} as {}", fileName, versionedFileName);

            return versionedFileName;
        } catch (IOException e) {
            logger.error("Error storing file: {}", fileName, e);
            throw new RuntimeException("Could not store file " + fileName, e);
        }
    }

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

    public Boolean deleteFile(String fileName) {
        try {
            if (fileName.equals(defaultProfilePicture)) {
                return false; // Do not allow deletion of the default profile picture
            }
            Path filePath = fileStoragePath.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);

        } catch (IOException e) {
            logger.error("Error deleting file: {}", fileName, e);
            return false;
        }
    }

    public String getDefaultProfilePicture() {
        return defaultProfilePicture;
    }

    /**
     * Stores a profile picture after resizing it to the target dimensions.
     * 
     * @param fileContent The raw byte content of the image file
     * @param fileName Original file name
     * @param fileExtension File extension (jpg, png, etc.)
     * @param mimeType The MIME type of the image
     * @return The unique name of the stored file
     */
    public String storeProfilePicture(byte[] fileContent, String fileName, String fileExtension,
            String mimeType) {
        try {
            // Validate file as before (this is handled in storeFile method)
            logger.info("Resizing profile picture: {} ({} bytes)", fileName, fileContent.length);

            // Resize image for profile (300x300)
            byte[] resizedContent = resizeImage(fileContent, 300, 300);

            // Log size reduction
            int originalSize = fileContent.length;
            int newSize = resizedContent.length;
            double reductionPercent = ((originalSize - newSize) / (double) originalSize) * 100.0;
            logger.info("Image resized: {} bytes to {} bytes (reduced by {:.2f}%)",
                    originalSize, newSize, reductionPercent);

            // Continue with storing as before
            return storeFile(resizedContent, fileName, fileExtension, mimeType);
        } catch (Exception e) {
            logger.error("Error processing profile picture: {}", fileName, e);
            throw new RuntimeException("Could not process profile picture " + fileName, e);
        }
    }

    /**
     * Resizes an image to the specified dimensions.
     * 
     * @param imageData The raw byte content of the image
     * @param targetWidth The desired width in pixels
     * @param targetHeight The desired height in pixels
     * @return The raw byte content of the resized image
     */
    private byte[] resizeImage(byte[] imageData, int targetWidth, int targetHeight) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                logger.warn("Could not read image for resizing, returning original");
                return imageData;
            }

            // Calculate dimensions that preserve aspect ratio
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();

            // Calculate scaling factors
            double widthScale = (double) targetWidth / originalWidth;
            double heightScale = (double) targetHeight / originalHeight;

            // Use the larger scaling factor to ensure the image covers the entire target area
            double scale = Math.max(widthScale, heightScale);

            // Calculate new dimensions
            int scaledWidth = (int) (originalWidth * scale);
            int scaledHeight = (int) (originalHeight * scale);

            // Create image with target dimensions
            BufferedImage resized =
                    new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resized.createGraphics();

            // Set white background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, targetWidth, targetHeight);

            // Enable high quality rendering
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Center the image
            int x = (targetWidth - scaledWidth) / 2;
            int y = (targetHeight - scaledHeight) / 2;

            // Draw the image
            g.drawImage(image, x, y, scaledWidth, scaledHeight, null);
            g.dispose();

            // Convert to PNG format
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(resized, "png", bos);
            return bos.toByteArray();
        } catch (IOException e) {
            logger.error("Error resizing image", e);
            return imageData; // Return original if resizing fails
        }
    }
}
