package com.example.te_demo.controller;

import com.example.te_demo.entity.AutoApprovalRule;
import com.example.te_demo.repository.AutoApprovalRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config/auto-approval-rules")
public class AutoApprovalRuleController {

    @Autowired
    private AutoApprovalRuleRepository repo;

    @GetMapping
    public List<AutoApprovalRule> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AutoApprovalRule body) {
        return ResponseEntity.ok(repo.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody AutoApprovalRule body) {
        return repo.findById(id).map(existing -> {
            existing.setCategory(body.getCategory());
            existing.setConditionField(body.getConditionField());
            existing.setConditionOp(body.getConditionOp());
            existing.setThresholdValue(body.getThresholdValue());
            existing.setMode(body.getMode());
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
