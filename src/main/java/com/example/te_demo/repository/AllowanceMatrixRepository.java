package com.example.te_demo.repository;

import com.example.te_demo.entity.AllowanceMatrix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AllowanceMatrixRepository extends JpaRepository<AllowanceMatrix, Integer> {
    List<AllowanceMatrix> findByGradeId(Long gradeId);
}
