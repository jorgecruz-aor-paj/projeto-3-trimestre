package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for {@code TrainingArea} entity.
 * <p>
 * Represents a thematic area or category under which trainings are organized.
 * This DTO is used to simplify references between trainings, filters, or dashboards.
 * JAXB annotations allow for XML serialization, making this class suitable for APIs
 * that support XML payloads.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier for the training area
 * - {@code name}: Human-readable name or label for the area (e.g., "Cybersecurity", "Leadership")
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement(name = "trainingArea")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrainingAreaDTO {

    /** Unique identifier of the training area */
    @XmlElement(name = "id")
    private Long id;

    /** Display name of the training area */
    @XmlElement(name = "name")
    private String name;

    public TrainingAreaDTO() {
    }

    public TrainingAreaDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
