package com.example.te_demo.service;

import com.example.te_demo.config.TenantMigrationRunner;
import com.example.te_demo.dto.OnboardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ClientOnboardingService {

    @Autowired
    private TenantMigrationRunner tenantMigrationRunner;

    public Map<String, String> onboard(OnboardRequest req) throws Exception {
        String schema = req.getTenantId();
        String clientName = req.getClientName();

        if (schema == null || schema.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (clientName == null || clientName.isBlank()) {
            throw new IllegalArgumentException("clientName is required");
        }

        if (tenantMigrationRunner.tenantSchemaExists(schema)) {
            throw new IllegalArgumentException("Tenant already exists: " + schema);
        }

        tenantMigrationRunner.onboardTenant(schema, clientName);

        return Map.of(
                "schemaName", schema,
                "clientName", clientName,
                "status", "ACTIVE"
        );
    }
}
