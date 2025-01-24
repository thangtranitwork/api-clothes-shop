package com.clothes.noc.repository;

import com.clothes.noc.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, String> {
    @Query("SELECT COUNT(pv) FROM Color c JOIN c.productVariants pv WHERE c.code = :code")
    int countVariantByCode(String code);
    boolean existsByCodeOrName(String code, String name);
}
