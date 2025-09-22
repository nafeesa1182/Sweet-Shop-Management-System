package com.sweetshop.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sweet_id", nullable = false)
    @JsonIgnore
    private Sweet sweet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    public CartItem() {}

    public CartItem(Sweet sweet, User user, int quantity, double price) {
        this.sweet = sweet;
        this.user = user;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() { return id; }
    public Sweet getSweet() { return sweet; }
    public User getUser() { return user; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void setSweet(Sweet sweet) { this.sweet = sweet; }
    public void setUser(User user) { this.user = user; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
}
