package com.example.te_demo.controller;

import com.example.te_demo.entity.CityTier;
import com.example.te_demo.repository.CityTierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config/city-tiers")
public class CityTierController {

    @Autowired
    private CityTierRepository repo;

    @GetMapping
    public List<CityTier> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CityTier body) {
        return ResponseEntity.ok(repo.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody CityTier body) {
        return repo.findById(id).map(existing -> {
            existing.setCityName(body.getCityName());
            existing.setTier(body.getTier());
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
