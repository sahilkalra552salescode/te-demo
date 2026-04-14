package com.example.te_demo.controller;

import com.example.te_demo.entity.Grade;
import com.example.te_demo.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeRepository gradeRepository;

    @GetMapping
    public List<Grade> getAll() {
        return gradeRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Grade grade) {
        return ResponseEntity.ok(gradeRepository.save(grade));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Grade body) {
        return gradeRepository.findById(id).map(existing -> {
            existing.setName(body.getName());
            return ResponseEntity.ok(gradeRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!gradeRepository.existsById(id)) return ResponseEntity.notFound().build();
        gradeRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("deleted", id));
    }
}
