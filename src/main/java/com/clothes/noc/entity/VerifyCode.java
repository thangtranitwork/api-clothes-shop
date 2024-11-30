package com.clothes.noc.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class VerifyCode {
    @Id
    String code;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    User user;
    @Column(nullable = false)
    LocalDateTime expiryTime;
    @Builder.Default
    @Column(nullable = false)
    boolean verified = false;
}
