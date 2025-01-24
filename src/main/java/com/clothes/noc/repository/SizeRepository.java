package com.clothes.noc.repository;

import com.clothes.noc.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, String> {
    @Query("SELECT COUNT(pv) FROM Size s JOIN s.productVariants pv WHERE s.name = :name")
    int countVariantByName(String name);
}
