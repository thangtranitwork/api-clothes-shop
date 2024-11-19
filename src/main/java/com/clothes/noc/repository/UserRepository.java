package com.clothes.noc.repository;

import com.clothes.noc.entity.Platform;
import com.clothes.noc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndPlatform(String email, Platform platform);
    boolean existsByEmailAndPlatform(String email, Platform platform);
}
