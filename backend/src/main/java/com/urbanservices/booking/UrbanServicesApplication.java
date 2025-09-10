package com.urbanservices.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "com.urbanservices.booking.model")
@EnableJpaRepositories(basePackages = "com.urbanservices.booking.repository")
@EnableTransactionManagement
@EnableJpaAuditing
public class UrbanServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(UrbanServicesApplication.class, args);
    }
}
