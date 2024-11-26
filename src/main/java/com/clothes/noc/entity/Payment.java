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
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    PaymentType type = PaymentType.CASH;
    @Column(nullable = false)
    Date payTime;
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    Order order;
}
