package aor.proj3t.Entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "settings")
public class SettingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "confirmation_window", nullable = false)
    private Integer confirmationWindow;

    @Column(name = "session_window", nullable = false)
    private Integer sessionWindow;

    @Column(name = "reset_password_window", nullable = false)
    private Integer resetPasswordWindow;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy;

}

