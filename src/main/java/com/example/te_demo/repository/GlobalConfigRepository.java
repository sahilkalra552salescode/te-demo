package com.example.te_demo.repository;

import com.example.te_demo.entity.GlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalConfigRepository extends JpaRepository<GlobalConfig, String> {
}
