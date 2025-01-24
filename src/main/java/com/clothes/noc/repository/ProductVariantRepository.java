package com.clothes.noc.repository;

import com.clothes.noc.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {
    boolean existsByProductIdAndColorCodeAndSizeName(String productId, String colorCode, String sizeName);
    @Query(value = "SELECT pv.quantity FROM ProductVariant pv WHERE pv.id = :variantId")
    Optional<Integer> getQuantityById(String variantId);
}
