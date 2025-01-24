package com.clothes.noc.entity;

import com.clothes.noc.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(exclude = "order")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String transactionId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    PaymentType type = PaymentType.CASH;
    LocalDateTime payTime;
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    Order order;
}
