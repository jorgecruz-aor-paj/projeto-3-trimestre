package aor.proj3t.Entities;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.management.relation.Role;
import java.time.LocalDateTime;

@Entity
@Table(name = "Users")

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id", nullable = false, unique = true, updatable = false)
    private long id;

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "phonenumber", nullable = false, unique = true)
    private String phoneNumber;

    @Column (name = "address", nullable = false)
    private String address;

    @Column (name = "birth_date", nullable = false)
    private LocalDateTime birthDate;

    @Column (name = "biography")
    private String biography;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "photograph", nullable = false)
    private String photograph;

    @Column(name = "role", nullable = false, unique = false, updatable = true)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private UserEntity manager;

    @ManyToOne
    @JoinColumn(name = "workplace_id")
    private WorkplaceEntity workplace;

}
