package aor.proj3t.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Entity representing a training area or category for organizing trainings.
 * Training areas help categorize and group related training courses
 * for better organization and filtering within the system.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "training_areas", indexes = {
        @Index(name = "idx_training_areas_name", columnList = "name")
})
public class TrainingArea {

    /**
     * Unique identifier for the training area.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the training area/category.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Default constructor for JPA.
     */
    public TrainingArea() {
    }

    /**
     * Constructor to create a training area with a name.
     * 
     * @param name the name of the training area
     */
    public TrainingArea(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier of this training area.
     * 
     * @return the training area ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this training area.
     * 
     * @param id the training area ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of this training area.
     * 
     * @return the training area name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this training area.
     * 
     * @param name the training area name
     */
    public void setName(String name) {
        this.name = name;
    }
}
