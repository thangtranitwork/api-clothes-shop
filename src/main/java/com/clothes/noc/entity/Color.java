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
public class Color {
    @Id
    @Column(columnDefinition = "char(7)")
    String code;
    @Column(nullable = false, unique = true)
    String name;
    @OneToMany(mappedBy = "color", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<ProductVariant> productVariants;
}
