package org.example.electricstore.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "APP_USER_UK", columnNames = "username") })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;
    private String username;
    private String encrytedPassword;
    private String email;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String employeeName;

    @Column(unique = true)
    private String employeeCode;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate employeeBirthday;

    private String employeeAddress;
    private String employeePhone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @PrePersist
    protected void dateBeforeCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void dateBeforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}