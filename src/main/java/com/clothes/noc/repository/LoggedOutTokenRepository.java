package com.clothes.noc.repository;

import com.clothes.noc.entity.LoggedOutToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedOutTokenRepository extends JpaRepository<LoggedOutToken, String> {
}
