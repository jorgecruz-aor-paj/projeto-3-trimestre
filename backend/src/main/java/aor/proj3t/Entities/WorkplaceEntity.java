package aor.proj3t.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Workplace")
public class WorkplaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
