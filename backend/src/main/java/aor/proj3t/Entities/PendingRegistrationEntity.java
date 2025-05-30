package aor.proj3t.Entities;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PendingRegistration")

public class PendingRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="id", nullable = false, unique = true, updatable = false)
    private long id;

    @Column(name = "email", nullable = false, unique = true,updatable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "confirmation_token", nullable = false)
    private String confirmationToken;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

}
