package com.sweetshop.demo.repository;

import com.sweetshop.demo.model.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SweetRepository extends JpaRepository<Sweet, Long> {

    // 🔍 Find sweet by name (used to prevent duplicates)
    Optional<Sweet> findByName(String name);
}
