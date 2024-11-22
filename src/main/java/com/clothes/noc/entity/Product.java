package com.clothes.noc.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    double price;
    @Column(nullable = false)
    String path;
    @Column(nullable = false)
    String img;
    @Column(nullable = false)
    String hoverImg;

    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    @ToString.Exclude
    ProductType type;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<ProductVariant> productVariants;
}
