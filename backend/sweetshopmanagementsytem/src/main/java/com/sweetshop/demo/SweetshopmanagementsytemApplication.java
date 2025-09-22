package com.sweetshop.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.sweetshop.demo.model")
@EnableJpaRepositories(basePackages = "com.sweetshop.demo.repository")
public class SweetshopmanagementsytemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SweetshopmanagementsytemApplication.class, args);
    }
}
