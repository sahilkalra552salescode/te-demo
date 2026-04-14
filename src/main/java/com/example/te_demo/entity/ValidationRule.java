package com.example.te_demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "validation_rules")
public class ValidationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "check_type", nullable = false, length = 50)
    private String checkType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
