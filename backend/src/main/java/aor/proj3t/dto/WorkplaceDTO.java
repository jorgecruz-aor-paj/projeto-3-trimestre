package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for {@code Workplace} entity.
 * <p>
 * Provides a lightweight representation of a workplace, typically used in
 * user profiles, organizational mappings, or dropdown filters. This DTO includes
 * essential attributes such as a unique identifier and the workplace name.
 * JAXB annotations support XML serialization for compatibility with external systems.
 * </p>
 *
 * Attributes:
 * - {@code id}: Unique identifier for the workplace record
 * - {@code name}: Human-readable name of the workplace (e.g., "Lisbon HQ", "Engineering Dept.")
 *
 * Author: Jorge Cruz & João Osório - Gr4  
 * Version: 1.0  
 * Since: 1.0
 */
@XmlRootElement(name = "workplace")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkplaceDTO {

    /** Unique identifier for the workplace */
    @XmlElement(name = "id")
    private Long id;

    /** Display name of the workplace location or unit */
    @XmlElement(name = "name")
    private String name;

    public WorkplaceDTO() {
    }

    public WorkplaceDTO(Long id, String name) {
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
