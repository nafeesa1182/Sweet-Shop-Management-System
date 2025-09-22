package com.sweetshop.demo.repository;

import com.sweetshop.demo.model.ERole;
import com.sweetshop.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // 🔍 Find role by enum name (e.g., ROLE_USER, ROLE_ADMIN)
    Optional<Role> findByName(ERole name);
}
