package aor.proj3t.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entity representing a workplace or office location within the organization.
 * Workplaces are used to group users by their physical or organizational
 * location,
 * providing context for employee management and reporting.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "workplace")
public class Workplace {

    /**
     * Unique identifier for the workplace.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the workplace/office location.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Default constructor for JPA.
     */
    public Workplace() {
    }

    /**
     * Gets the unique identifier of this workplace.
     * 
     * @return the workplace ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this workplace.
     * 
     * @param id the workplace ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of this workplace.
     * 
     * @return the workplace name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this workplace.
     * 
     * @param name the workplace name
     */
    public void setName(String name) {
        this.name = name;
    }
}
