package com.clothes.noc.repository;

import com.clothes.noc.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerifyEmailCodeRepository extends JpaRepository<VerifyCode, String> {
    Optional<VerifyCode> findByUserEmail(String email);
}
