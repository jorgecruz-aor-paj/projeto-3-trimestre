package aor.proj3t.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "training_areas")
public class TrainingAreaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
