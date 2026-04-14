package com.example.te_demo.controller;

import com.example.te_demo.entity.ExpenseFrequencyRule;
import com.example.te_demo.repository.ExpenseFrequencyRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config/expense-frequency-rules")
public class ExpenseFrequencyRuleController {

    @Autowired
    private ExpenseFrequencyRuleRepository repo;

    @GetMapping
    public List<ExpenseFrequencyRule> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ExpenseFrequencyRule body) {
        return ResponseEntity.ok(repo.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ExpenseFrequencyRule body) {
        return repo.findById(id).map(existing -> {
            existing.setCategory(body.getCategory());
            existing.setFrequencyType(body.getFrequencyType());
            existing.setMaxPerDay(body.getMaxPerDay());
            existing.setIsActive(body.getIsActive());
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
