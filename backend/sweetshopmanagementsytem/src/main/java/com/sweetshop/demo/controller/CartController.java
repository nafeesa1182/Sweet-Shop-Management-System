package com.sweetshop.demo.controller;

import com.sweetshop.demo.model.*;
import com.sweetshop.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private SweetRepository sweetRepo;

    @Autowired
    private UserRepository userRepo;

    // ---------- ADD TO CART ----------
    @PostMapping("/add/{sweetId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long sweetId,
            @RequestParam int quantity,
            Authentication auth
    ) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        User user = userRepo.findByUsername(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        Sweet sweet = sweetRepo.findById(sweetId).orElse(null);
        if (sweet == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sweet not found");

        int existingQty = cartItemRepo.findByUserIdAndSweetId(user.getId(), sweetId)
                .map(CartItem::getQuantity)
                .orElse(0);

        if (sweet.getQuantity() < existingQty + quantity) {
            return ResponseEntity.badRequest().body("Not enough stock");
        }

        CartItem item = cartItemRepo.findByUserIdAndSweetId(user.getId(), sweetId)
                .orElse(new CartItem());

        item.setSweet(sweet);
        item.setUser(user);
        item.setQuantity(item.getId() == null ? quantity : item.getQuantity() + quantity);
        item.setPrice(sweet.getPrice() * item.getQuantity());

        cartItemRepo.save(item);

        System.out.println("✅ Added sweet '" + sweet.getName() + "' to cart for user '" + user.getUsername() + "'");
        return ResponseEntity.ok("Sweet added to cart");
    }

    // ---------- VIEW CART ----------
    @GetMapping
    public ResponseEntity<?> viewCart(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing token");
        }

        User user = userRepo.findByUsername(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        List<CartItem> items = cartItemRepo.findByUserId(user.getId());

        List<Map<String, Object>> response = items.stream().map(item -> {
            Map<String, Object> dto = new HashMap<>();
            dto.put("itemId", item.getId());
            dto.put("sweetName", item.getSweet().getName());
            dto.put("quantity", item.getQuantity());
            dto.put("totalPrice", item.getPrice());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ---------- UPDATE QUANTITY ----------
    @PutMapping("/update/{sweetId}")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long sweetId,
            @RequestParam int quantity,
            Authentication auth
    ) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        User user = userRepo.findByUsername(auth.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        CartItem item = cartItemRepo.findByUserIdAndSweetId(user.getId(), sweetId).orElse(null);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found in cart");
        }

        Sweet sweet = sweetRepo.findById(sweetId).orElse(null);
        if (sweet == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sweet not found");
        }

        int totalAvailable = sweet.getQuantity();
        int currentInCart = item.getQuantity();

        // If increasing quantity, check stock
        if (quantity > currentInCart && totalAvailable < quantity) {
            return ResponseEntity.badRequest().body("Not enough stock to update quantity");
        }

        item.setQuantity(quantity);
        item.setPrice(sweet.getPrice() * quantity);
        cartItemRepo.save(item);

        System.out.println("🔄 Updated quantity of sweet '" + sweet.getName() + "' to " + quantity + " for user '" + user.getUsername() + "'");
        Map<String, Object> response = new HashMap<>();
        response.put("itemId", item.getId());
        response.put("sweetName", sweet.getName());
        response.put("newQuantity", quantity);
        response.put("totalPrice", item.getPrice());

        return ResponseEntity.ok(response);
    }


    // ---------- REMOVE ITEM ----------
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<?> removeItem(@PathVariable Long itemId, Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        Optional<CartItem> item = cartItemRepo.findById(itemId);
        if (item.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        }

        cartItemRepo.deleteById(itemId);
        System.out.println("🗑️ Removed item ID " + itemId + " from cart");
        return ResponseEntity.ok("Item removed");
    }

    // ---------- CLEAR CART ----------
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid token");
        }

        User user = userRepo.findByUsername(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        List<CartItem> items = cartItemRepo.findByUserId(user.getId());
        cartItemRepo.deleteAll(items);

        System.out.println("🧹 Cleared cart for user '" + user.getUsername() + "'");
        return ResponseEntity.ok("Cart cleared");
    }
}
