package com.example.te_demo.controller;

import com.example.te_demo.entity.Grade;
import com.example.te_demo.entity.RouteMatrix;
import com.example.te_demo.repository.GradeRepository;
import com.example.te_demo.repository.RouteMatrixRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config/route-matrix")
public class RouteMatrixController {

    @Autowired
    private RouteMatrixRepository repo;

    @Autowired
    private GradeRepository gradeRepo;

    @GetMapping
    public List<RouteMatrix> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RouteMatrix body) {
        return ResponseEntity.ok(repo.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody RouteMatrix body) {
        return repo.findById(id).map(existing -> {
            existing.setRouteCode(body.getRouteCode());
            existing.setFromTown(body.getFromTown());
            existing.setToTown(body.getToTown());
            existing.setDistanceKm(body.getDistanceKm());
            existing.setGradeId(body.getGradeId());
            existing.setTaPerKm(body.getTaPerKm());
            existing.setDaPerDay(body.getDaPerDay());
            existing.setNhPerNight(body.getNhPerNight());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok(Map.of("deleted", id));
    }

    // CSV template: route_code,from_town,to_town,distance_km,grade_name,ta_per_km,da_per_day,nh_per_night
    @PostMapping("/bulk-upload")
    public ResponseEntity<?> bulkUpload(@RequestParam("file") MultipartFile file) {
        int total = 0, imported = 0, skipped = 0;
        List<Map<String, String>> errors = new ArrayList<>();

        // Build grade name → id lookup
        Map<String, Long> gradeMap = new HashMap<>();
        for (Grade g : gradeRepo.findAll()) {
            gradeMap.put(g.getName().toLowerCase(), g.getId());
        }

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] header = reader.readNext(); // skip header
            String[] row;
            int rowNum = 1;
            while ((row = reader.readNext()) != null) {
                rowNum++;
                total++;
                try {
                    if (row.length < 8) {
                        errors.add(Map.of("row", String.valueOf(rowNum), "reason", "Insufficient columns"));
                        skipped++;
                        continue;
                    }
                    String routeCode = row[0].trim();
                    String fromTown  = row[1].trim();
                    String toTown    = row[2].trim();
                    String distStr   = row[3].trim();
                    String gradeName = row[4].trim().toLowerCase();
                    String taStr     = row[5].trim();
                    String daStr     = row[6].trim();
                    String nhStr     = row[7].trim();

                    if (routeCode.isEmpty() || fromTown.isEmpty() || toTown.isEmpty() || gradeName.isEmpty()) {
                        errors.add(Map.of("row", String.valueOf(rowNum), "reason", "Required fields empty"));
                        skipped++;
                        continue;
                    }

                    Long gradeId = gradeMap.get(gradeName);
                    if (gradeId == null) {
                        errors.add(Map.of("row", String.valueOf(rowNum), "reason", "Unknown grade: " + gradeName));
                        skipped++;
                        continue;
                    }

                    if (repo.existsByRouteCodeAndGradeId(routeCode, gradeId)) {
                        errors.add(Map.of("row", String.valueOf(rowNum), "reason", "Duplicate: " + routeCode + " / " + gradeName));
                        skipped++;
                        continue;
                    }

                    RouteMatrix rm = new RouteMatrix();
                    rm.setRouteCode(routeCode);
                    rm.setFromTown(fromTown);
                    rm.setToTown(toTown);
                    rm.setDistanceKm(new BigDecimal(distStr));
                    rm.setGradeId(gradeId);
                    rm.setTaPerKm(new BigDecimal(taStr));
                    rm.setDaPerDay(new BigDecimal(daStr));
                    rm.setNhPerNight(nhStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(nhStr));
                    repo.save(rm);
                    imported++;

                } catch (Exception e) {
                    errors.add(Map.of("row", String.valueOf(rowNum), "reason", e.getMessage()));
                    skipped++;
                }
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to parse CSV: " + e.getMessage()));
        }

        return ResponseEntity.ok(Map.of(
                "total", total,
                "imported", imported,
                "skipped", skipped,
                "errors", errors
        ));
    }
}
