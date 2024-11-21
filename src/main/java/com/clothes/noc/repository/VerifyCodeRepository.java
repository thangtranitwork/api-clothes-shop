package com.clothes.noc.repository;

import com.clothes.noc.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyCodeRepository extends JpaRepository<VerifyCode, String> {
}
