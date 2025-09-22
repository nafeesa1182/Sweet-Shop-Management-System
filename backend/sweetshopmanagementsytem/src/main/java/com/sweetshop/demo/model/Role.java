package com.sweetshop.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true, nullable = false)
    private ERole name;

    public Role() {}
    public Role(ERole name) { this.name = name; }

    // getters & setters
    public Long getId() { return id; }
    public ERole getName() { return name; }
    public void setName(ERole name) { this.name = name; }
}
