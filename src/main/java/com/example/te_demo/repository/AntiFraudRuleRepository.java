package com.example.te_demo.repository;

import com.example.te_demo.entity.AntiFraudRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AntiFraudRuleRepository extends JpaRepository<AntiFraudRule, Integer> {
}
