package com.clothes.noc.repository;

import com.clothes.noc.entity.Color;
import com.clothes.noc.entity.Product;
import com.clothes.noc.entity.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    @Query("""
            SELECT DISTINCT pv.color
            FROM ProductVariant pv
            WHERE pv.product.id = :productId
            """)
    List<Color> findAllColorsOfAProduct(String productId);

    @Query("""
                SELECT DISTINCT pv.color
                FROM ProductVariant pv
                WHERE pv.product.type.type = :type
            """)
    List<Color> findAllColorsOfAType(String type);

    @Query("""
                SELECT DISTINCT pv.color
                FROM ProductVariant pv
                WHERE pv.product.type.subtype = :subtype
            """)
    List<Color> findAllColorsOfASubType(String subtype);

    @Query("""
            SELECT DISTINCT pv.size
            FROM ProductVariant pv
            WHERE pv.product.id = :productId
            """)
    List<Size> findAllSizesOfAProduct(String productId);
    @Query("""
                SELECT DISTINCT pv.size
                FROM ProductVariant pv
                WHERE pv.product.type.type = :type
            """)
    List<Size> findAllSizesOfAType(String type);

    @Query("""
                SELECT DISTINCT pv.size
                FROM ProductVariant pv
                WHERE pv.product.type.subtype = :subtype
            """)
    List<Size> findAllSizesOfASubType(String subtype);

    Optional<Product> findByPath(String path);

    @Query("""
                SELECT p
                FROM Product p
            """)
    Page<Product> findHotProducts(Pageable pageable);
}
