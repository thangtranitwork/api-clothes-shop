package com.clothes.noc.repository;

import com.clothes.noc.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
    boolean existsBySubtype(String subtype);
    @Query("SELECT count(*) FROM Product p WHERE p.type.id=:id"
    )
    int countProductById(String id);
}
