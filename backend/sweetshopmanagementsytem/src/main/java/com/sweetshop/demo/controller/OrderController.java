package com.sweetshop.demo.controller;

import com.sweetshop.demo.model.*;
import com.sweetshop.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    @Autowired private UserRepository userRepository;
    @Autowired private SweetRepository sweetRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private OrderRepository orderRepository;

    @GetMapping("/admin/orders")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllOrdersForAdmin(Authentication Auth) {
        System.out.println("🔍 Authenticated user: " + Auth.getName());
        System.out.println("🔍 Authorities: " + Auth.getAuthorities());

        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/place")
    @Transactional
    public ResponseEntity<?> placeOrder(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new RuntimeException("User not found"));

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());
        if (cartItems == null || cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cartItems) {
            Sweet sweet = cartItem.getSweet();
            if (sweet == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sweet not found");
            }

            if (sweet.getQuantity() < cartItem.getQuantity()) {
                return ResponseEntity.badRequest().body("Not enough stock for " + sweet.getName());
            }

            sweet.setQuantity(sweet.getQuantity() - cartItem.getQuantity());
            sweetRepository.save(sweet);

            OrderItem item = new OrderItem();
            item.setSweet(sweet);
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(sweet.getPrice());
            item.setOrder(order);

            orderItems.add(item);
            total += sweet.getPrice() * cartItem.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);
        orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        return ResponseEntity.ok("Order placed successfully");
    }

    @PutMapping("/{id}/pay")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> markOrderAsPaid(@PathVariable Long id, Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order ID " + id + " not found");
        }


        if ("PAID".equals(order.getStatus())) {
            return ResponseEntity.badRequest().body("Order is already marked as PAID");
        }

        if (auth.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            if (!order.getUser().getUsername().equals(auth.getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only pay for your own orders");
            }
        }

        order.setStatus("PAID");
        orderRepository.save(order);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getId());
        response.put("status", order.getStatus());
        response.put("message", "Order marked as PAID");

        return ResponseEntity.ok(response);
    }


    @GetMapping("/history/status/{status}")
    public ResponseEntity<?> getOrdersByStatus(@PathVariable String status, Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() ->
                new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUserIdAndStatus(user.getId(), status.toUpperCase());
        return ResponseEntity.ok(orders);
    }
}
