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
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_product_color_size",
                        columnNames = {"product_id", "size_name", "color_code"})
        }
)
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Column(nullable = false)
    int quantity;
    @Column(nullable = false)
    String img;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    Product product;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<CartItem> cartItems;

    @ManyToOne
    @JoinColumn(name = "size_name", nullable = false)
    @ToString.Exclude
    Size size;
    @ManyToOne
    @JoinColumn(name = "color_code", nullable = false)
    @ToString.Exclude
    Color color;
}
