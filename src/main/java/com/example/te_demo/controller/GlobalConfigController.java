package com.example.te_demo.controller;

import com.example.te_demo.entity.GlobalConfig;
import com.example.te_demo.repository.GlobalConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config/global-config")
public class GlobalConfigController {

    @Autowired
    private GlobalConfigRepository repo;

    @GetMapping
    public List<GlobalConfig> getAll() {
        return repo.findAll();
    }

    @PutMapping("/{key}")
    public ResponseEntity<?> upsert(@PathVariable String key, @RequestBody Map<String, String> body) {
        String value = body.get("configValue");
        String updatedByStr = body.get("updatedBy");
        if (value == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "configValue is required"));
        }

        GlobalConfig cfg = repo.findById(key).orElse(new GlobalConfig());
        cfg.setConfigKey(key);
        cfg.setConfigValue(value);
        cfg.setUpdatedAt(LocalDateTime.now());
        if (updatedByStr != null) {
            cfg.setUpdatedBy(Long.parseLong(updatedByStr));
        }
        return ResponseEntity.ok(repo.save(cfg));
    }
}
