package com.sweetshop.demo.controller;

import com.sweetshop.demo.model.Sweet;
import com.sweetshop.demo.repository.SweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sweets")
public class SweetController {

    @Autowired
    private SweetRepository sweetRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Sweet>> getAllSweets() {
        List<Sweet> sweets = sweetRepository.findAll();
        return ResponseEntity.ok(sweets);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addSweet(@RequestBody Sweet sweet) {
        System.out.println("🔐 Roles: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        System.out.println("📥 Received sweet: " + sweet);

        // Basic validation
        if (sweet.getName() == null || sweet.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Sweet name is required");
        }
        if (sweet.getPrice() <= 0) {
            return ResponseEntity.badRequest().body("Price must be greater than 0");
        }
        if (sweet.getQuantity() < 0) {
            return ResponseEntity.badRequest().body("Quantity cannot be negative");
        }

        // Check for duplicate sweet
        if (sweetRepository.findByName(sweet.getName()).isPresent()) {
            return ResponseEntity.badRequest().body("Sweet already exists!");
        }

        // Save sweet
        sweetRepository.save(sweet);
        System.out.println("✅ Sweet '" + sweet.getName() + "' added successfully");

        return ResponseEntity.ok("Sweet added successfully!");
    }


    @PutMapping("/admin/update-stock/{sweetId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateStock(
            @PathVariable Long sweetId,
            @RequestParam int quantity
    ) {
        Sweet sweet = sweetRepository.findById(sweetId).orElse(null);
        if (sweet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sweet not found");
        }

        sweet.setQuantity(quantity);
        sweetRepository.save(sweet);

        System.out.println("📦 Stock updated for sweet '" + sweet.getName() + "' to " + quantity);
        return ResponseEntity.ok("Stock updated successfully");
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteSweet(@PathVariable Long id) {
        if (!sweetRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sweet not found");
        }

        sweetRepository.deleteById(id);
        return ResponseEntity.ok("Sweet deleted successfully!");
    }
}
