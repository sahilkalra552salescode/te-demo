package com.example.te_demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "auto_approval_rules")
public class AutoApprovalRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String category;

    @Column(name = "condition_field", length = 100)
    private String conditionField;

    @Column(name = "condition_op", length = 30)
    private String conditionOp;

    @Column(name = "threshold_value", length = 100)
    private String thresholdValue;

    @Column(nullable = false, length = 10)
    private String mode = "AUTO";

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
