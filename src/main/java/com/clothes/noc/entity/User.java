package com.clothes.noc.entity;

import com.clothes.noc.enums.Platform;
import com.clothes.noc.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "cart")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "usr",
        uniqueConstraints = {@UniqueConstraint(name = "unique_email_platform", columnNames = {"email", "platform"})}
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    String firstname;
    @Column(nullable = false)
    String lastname;
    @Column(nullable = false)
    String email;
    String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    Role role = Role.USER;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    Platform platform = Platform.APP;
    @Builder.Default
    @Column(nullable = false)
    boolean isVerified = false;
    boolean accountLocked;
    @Column(nullable = false)
    @Builder.Default
    int failedAttempts = 0;
    LocalDateTime lockoutTime;
    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    VerifyCode verifyCode;
    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    Cart cart;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<Order> orders;
}
