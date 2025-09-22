package com.sweetshop.demo.repository;

import com.sweetshop.demo.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 🔍 Fetch all orders for a specific user
    List<Order> findByUserId(Long userId);

    // 🔍 Fetch orders by user and status (e.g. PLACED, PAID)
    List<Order> findByUserIdAndStatus(Long userId, String status);
}
