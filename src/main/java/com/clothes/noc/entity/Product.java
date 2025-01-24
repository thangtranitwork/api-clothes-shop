package com.clothes.noc.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    String name;
    @Column(columnDefinition = "MEDIUMTEXT", nullable = false)
    String description;
    @Column(nullable = false)
    int price;
    @Column(nullable = false)
    String path;
    @Column(nullable = false)
    String img;
    @Column(nullable = false)
    String hoverImg;
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    @ToString.Exclude
    ProductType type;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<ProductVariant> productVariants;
}
