package com.sweetshop.demo.repository;

import com.sweetshop.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 🔍 Fetch all cart items for a specific user
    List<CartItem> findByUserId(Long userId);

    // 🔍 Fetch a specific cart item by user and sweet
    Optional<CartItem> findByUserIdAndSweetId(Long userId, Long sweetId);
}
