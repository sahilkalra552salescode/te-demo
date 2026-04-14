package com.example.te_demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "allowance_matrix",
        uniqueConstraints = @UniqueConstraint(columnNames = {"grade_id", "tier"}))
public class AllowanceMatrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "grade_id", nullable = false)
    private Long gradeId;

    @Column(nullable = false)
    private Short tier;

    @Column(name = "da_rate", nullable = false, precision = 10, scale = 2)
    private BigDecimal daRate;

    @Column(name = "ta_rate", nullable = false, precision = 8, scale = 4)
    private BigDecimal taRate;

    @Column(name = "meal_limit", nullable = false, precision = 10, scale = 2)
    private BigDecimal mealLimit = BigDecimal.ZERO;

    @Column(name = "night_halt_limit", nullable = false, precision = 10, scale = 2)
    private BigDecimal nightHaltLimit = BigDecimal.ZERO;
}
