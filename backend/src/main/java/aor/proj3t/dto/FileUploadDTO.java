package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for file uploads.
 * <p>
 * Represents metadata and binary content for uploaded files, typically
 * consumed by REST endpoints that support multipart or base64-encoded
 * file transmission. Includes fields for filename, MIME type, extension,
 * and raw content. JAXB-compatible for XML marshalling if required.
 * </p>
 *
 * Attributes:
 * - {@code fileName}: Original name of the file (without path)
 * - {@code fileContent}: Binary content of the file as a byte array
 * - {@code fileMimeType}: MIME type string (e.g. "image/png", "application/pdf")
 * - {@code fileExtension}: File extension (e.g. "png", "jpg", "pdf")
 *
 *
 * Common use cases include profile picture uploads, document storage, and
 * temporary in-memory file handling before persistence.
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "fileUpload")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileUploadDTO {

    /** Original name of the uploaded file */
    private String fileName;

    /** Raw binary content of the file */
    private byte[] fileContent;

    /** MIME type of the file (e.g., image/jpeg) */
    private String fileMimeType;

    /** Extension of the file without dot (e.g., png, pdf) */
    private String fileExtension;

    public FileUploadDTO() {
        // Default constructor
    } 

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileMimeType() {
        return fileMimeType;
    }

    public void setFileMimeType(String fileMimeType) {
        this.fileMimeType = fileMimeType;
    }

}
