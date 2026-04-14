package com.example.te_demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "expense_frequency_rules")
public class ExpenseFrequencyRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String category;

    @Column(name = "frequency_type", nullable = false, length = 20)
    private String frequencyType;

    @Column(name = "max_per_day")
    private Integer maxPerDay;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
