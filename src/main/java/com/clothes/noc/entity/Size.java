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
public class Size {
    @Id
    String name;
    @OneToMany(mappedBy = "size", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<ProductVariant> productVariants;
}
