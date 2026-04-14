package com.example.te_demo.controller;

import com.example.te_demo.entity.AllowanceMatrix;
import com.example.te_demo.repository.AllowanceMatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config/allowance-matrix")
public class AllowanceMatrixController {

    @Autowired
    private AllowanceMatrixRepository repo;

    @GetMapping
    public List<AllowanceMatrix> getAll() {
        return repo.findAll();
    }

    @GetMapping("/grade/{gradeId}")
    public List<AllowanceMatrix> getByGrade(@PathVariable Long gradeId) {
        return repo.findByGradeId(gradeId);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AllowanceMatrix body) {
        return ResponseEntity.ok(repo.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody AllowanceMatrix body) {
        return repo.findById(id).map(existing -> {
            existing.setGradeId(body.getGradeId()); // Long
            existing.setTier(body.getTier());
            existing.setDaRate(body.getDaRate());
            existing.setTaRate(body.getTaRate());
            existing.setMealLimit(body.getMealLimit());
            existing.setNightHaltLimit(body.getNightHaltLimit());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("deleted", id));
    }
}
