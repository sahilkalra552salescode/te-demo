package com.example.te_demo.controller;

import com.example.te_demo.entity.ValidationRule;
import com.example.te_demo.repository.ValidationRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config/validation-rules")
public class ValidationRuleController {

    @Autowired
    private ValidationRuleRepository repo;

    @GetMapping
    public List<ValidationRule> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ValidationRule body) {
        return ResponseEntity.ok(repo.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ValidationRule body) {
        return repo.findById(id).map(existing -> {
            existing.setCategory(body.getCategory());
            existing.setCheckType(body.getCheckType());
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
