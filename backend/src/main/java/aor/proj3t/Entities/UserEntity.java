package aor.proj3t.Entities;


import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDate birthDate;

    @Column (name = "biography")
    private String biography;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "photograph", nullable = false)
    private String photograph;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, unique = false, updatable = true)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private UserEntity manager;

    @OneToMany
    @JoinColumn(name = "manager")
    private List<UserEntity> managedUsers;

    @ManyToOne
    @JoinColumn(name = "workplace_id")
    private WorkplaceEntity workplace;

    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
    private List<NotificationEntity> receivedNotifications;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<NotificationEntity> sentNotifications;

    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
    private List<MessageEntity> receivedMessages;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<MessageEntity> sentMessages;

}
