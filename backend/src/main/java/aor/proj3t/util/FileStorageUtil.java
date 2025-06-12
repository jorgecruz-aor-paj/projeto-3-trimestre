package aor.proj3t.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileStorageUtil {

    private static final Logger logger = LogManager.getLogger(FileStorageUtil.class);

    private Path fileStoragePath;
    private String defaultProfilePicture = "default-profile-picture.png";

    @PostConstruct
    public void init() {
        try {
            String uploadDir = System.getProperty("jboss.server.data.dir") + File.separator + "profile-pictures";
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

}
