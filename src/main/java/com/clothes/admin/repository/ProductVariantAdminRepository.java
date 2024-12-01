package com.clothes.admin.repository;

import com.clothes.noc.entity.ProductVariant;
import com.clothes.noc.repository.ProductVariantRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantAdminRepository extends ProductVariantRepository {

    @Query(value = "SELECT * FROM product_variant pv WHERE pv.product_id = :productId", nativeQuery = true)
    List<ProductVariant> findByProductIdNative(@Param("productId") String productId);

    @Query(value = "SELECT * FROM product_variant pv WHERE pv.color_code = :colorCode", nativeQuery = true)
    List<ProductVariant> findByColorCodeNative(@Param("colorCode") String colorCode);

    @Query(value = "SELECT * FROM product_variant pv WHERE pv.size_id = :sizeId", nativeQuery = true)
    List<ProductVariant> findBySizeIdNative(@Param("sizeId") String sizeId);
}
