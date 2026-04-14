package com.example.te_demo.repository;

import com.example.te_demo.entity.ExpenseFrequencyRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseFrequencyRuleRepository extends JpaRepository<ExpenseFrequencyRule, Integer> {
}
