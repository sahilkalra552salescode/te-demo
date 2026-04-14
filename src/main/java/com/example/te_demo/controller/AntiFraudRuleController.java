package com.example.te_demo.controller;

import com.example.te_demo.entity.AntiFraudRule;
import com.example.te_demo.repository.AntiFraudRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config/anti-fraud-rules")
public class
AntiFraudRuleController {

    @Autowired
    private AntiFraudRuleRepository repo;

    @GetMapping
    public List<AntiFraudRule> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AntiFraudRule body) {
        return ResponseEntity.ok(repo.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody AntiFraudRule body) {
        return repo.findById(id).map(existing -> {
            existing.setDescription(body.getDescription());
            existing.setCondition(body.getCondition());
            existing.setRuleType(body.getRuleType());
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
