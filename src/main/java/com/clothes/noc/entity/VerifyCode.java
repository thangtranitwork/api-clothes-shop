package com.clothes.noc.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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
    User user;
    Date expiryTime;
    @Enumerated(EnumType.STRING)
    VerifyAction verifyAction;
}
