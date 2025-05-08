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
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String username;

    @Column(name = "encryted_password", nullable = false)
    private String encrytedPassword;

    @Column(name = "employee_email", nullable = false)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "employee_birthday", nullable = false)
    private LocalDate employeeBirthday;

    @Column(name = "employee_address", nullable = false)
    private String employeeAddress;

    @Column(name = "employee_phone", nullable = false)
    private String employeePhone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false))
    private List<Role> roles;

    @PrePersist
    protected void dateBeforeCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void dateBeforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}