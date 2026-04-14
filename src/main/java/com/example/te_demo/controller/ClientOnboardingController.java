package com.example.te_demo.controller;

import com.example.te_demo.dto.OnboardRequest;
import com.example.te_demo.service.ClientOnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/clients")
public class ClientOnboardingController {

    @Autowired
    private ClientOnboardingService onboardingService;

    @PostMapping("/onboard")
    public ResponseEntity<?> onboard(@RequestBody OnboardRequest req) {
        try {
            Map<String, String> result = onboardingService.onboard(req);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
