package com.example.te_demo.repository;

import com.example.te_demo.entity.RouteMatrix;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteMatrixRepository extends JpaRepository<RouteMatrix, Integer> {
    boolean existsByRouteCodeAndGradeId(String routeCode, Long gradeId);
}
