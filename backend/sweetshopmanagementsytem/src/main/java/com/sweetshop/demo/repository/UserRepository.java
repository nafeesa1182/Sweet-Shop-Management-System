package com.sweetshop.demo.repository;

import com.sweetshop.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Find user by username (used in login and profile)
    Optional<User> findByUsername(String username);

    // 🔍 Find user by email (used in signup and validation)
    Optional<User> findByEmail(String email);

    // ✅ Check if username already exists
    Boolean existsByUsername(String username);

    // ✅ Check if email already exists
    Boolean existsByEmail(String email);
}
