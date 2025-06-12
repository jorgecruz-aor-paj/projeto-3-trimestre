package aor.proj3t.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
    private final String defaultProfilePictureDir = AppConstants.FileStorage.DEFAULT_PROFILE_PICTURE_DIR;

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

    public String storeFile(byte[] fileContent, String fileName, String fileExtension) {
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

            // TODO: Validar MIME type da imagem

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

}
