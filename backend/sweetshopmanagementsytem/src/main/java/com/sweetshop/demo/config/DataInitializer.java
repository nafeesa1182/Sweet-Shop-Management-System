package com.sweetshop.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sweetshop.demo.model.ERole;
import com.sweetshop.demo.model.Role;
import com.sweetshop.demo.repository.RoleRepository;

import jakarta.transaction.Transactional;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_USER));
                System.out.println("✅ ROLE_USER inserted");
            } else {
                System.out.println("ℹ️ ROLE_USER already exists");
            }

            if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(ERole.ROLE_ADMIN));
                System.out.println("✅ ROLE_ADMIN inserted");
            } else {
                System.out.println("ℹ️ ROLE_ADMIN already exists");
            }
        };
    }
}
