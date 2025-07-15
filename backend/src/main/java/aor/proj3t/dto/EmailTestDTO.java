package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * DTO for testing email functionality.
 * 
 * This class represents a request to test the email sending feature.
 * It includes fields for email recipient, subject, and different types of
 * tests.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "emailTest")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailTestDTO {

    @XmlElement(name = "recipient")
    private String recipient;

    @XmlElement(name = "subject")
    private String subject;

    @XmlElement(name = "message")
    private String message;

    @XmlElement(name = "testType")
    private String testType; // "simple", "confirmation", "reset"

    @XmlElement(name = "baseUrl")
    private String baseUrl;

    public EmailTestDTO() {
    }

    public EmailTestDTO(String recipient, String subject, String message, String testType, String baseUrl) {
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.testType = testType;
        this.baseUrl = baseUrl;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
