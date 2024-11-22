package com.clothes.noc.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    @Builder.Default
    int quantity = 0;
    @Column(nullable = false)
    double price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    Order order;

    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    @ToString.Exclude
    ProductVariant productVariant;
}
