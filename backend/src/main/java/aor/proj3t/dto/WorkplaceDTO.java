package aor.proj3t.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Data Transfer Object for Workplace entity
 */
@XmlRootElement(name = "workplace")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkplaceDTO {
    @XmlElement(name = "id")
    private Long id;
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
