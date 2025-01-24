package com.clothes.noc.repository;

import com.clothes.noc.enums.Platform;
import com.clothes.noc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailAndPlatform(String email, Platform platform);
    boolean existsByEmailAndPlatform(String email, Platform platform);
    @Query("SELECT u.role FROM User u WHERE u.id=?1")
    Optional<String> getRoleById(String id);
    @Query("SELECT u.id FROM User u WHERE u.email=?1 AND u.platform=?2")
    Optional<String> getIdByEmailAndPlatform(String email, Platform platform);
}
