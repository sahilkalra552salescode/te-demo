package com.example.te_demo.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class TenantMigrationRunner implements ApplicationRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SchemaPerTenantConnectionProvider connectionProvider;

    @Value("${tenants.schemas}")
    private List<String> configuredSchemas;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=== Starting tenant migrations ===");

        // 1. Ensure public.tenants table exists
        ensureTenantsTable();

        // 2. Seed yml-defined schemas into public.tenants if missing (backward compat)
        for (String schema : configuredSchemas) {
            seedTenantIfMissing(schema, schema);
        }

        // 3. Load all active tenants from DB and migrate each
        List<String> activeSchemas = loadActiveTenantSchemas();
        for (String schema : activeSchemas) {
            System.out.println("Migrating: " + schema);
            runFlywayForSchema(schema);
            connectionProvider.registerTenant(schema);
            System.out.println("Done: " + schema);
        }

        System.out.println("=== All migrations complete ===");
    }

    public void runFlywayForSchema(String schema) {
        Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .locations("classpath:db/migration")
                .createSchemas(true)
                .baselineOnMigrate(true)
                .load()
                .migrate();
    }

    private void ensureTenantsTable() throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS public.tenants (
                        id          SERIAL        PRIMARY KEY,
                        schema_name VARCHAR(50)   NOT NULL UNIQUE,
                        client_name VARCHAR(100)  NOT NULL,
                        status      VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
                        created_at  TIMESTAMP     NOT NULL DEFAULT NOW()
                    )
                    """);
        }
    }

    private void seedTenantIfMissing(String schema, String clientName) throws Exception {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement check = conn.prepareStatement(
                     "SELECT 1 FROM public.tenants WHERE schema_name = ?")) {
            check.setString(1, schema);
            try (ResultSet rs = check.executeQuery()) {
                if (!rs.next()) {
                    try (PreparedStatement insert = conn.prepareStatement(
                            "INSERT INTO public.tenants(schema_name, client_name) VALUES (?, ?)")) {
                        insert.setString(1, schema);
                        insert.setString(2, clientName);
                        insert.executeUpdate();
                    }
                }
            }
        }
    }

    public void onboardTenant(String schema, String clientName) throws Exception {
        // Create schema
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE SCHEMA IF NOT EXISTS " + schema);
        }
        // Insert into public.tenants
        seedTenantIfMissing(schema, clientName);
        // Run migrations
        runFlywayForSchema(schema);
        // Register HikariCP pool
        connectionProvider.registerTenant(schema);
    }

    private List<String> loadActiveTenantSchemas() throws Exception {
        List<String> schemas = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT schema_name FROM public.tenants WHERE status = 'ACTIVE'")) {
            while (rs.next()) {
                schemas.add(rs.getString("schema_name"));
            }
        }
        return schemas;
    }

    public boolean tenantSchemaExists(String schema) throws Exception {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT 1 FROM public.tenants WHERE schema_name = ?")) {
            stmt.setString(1, schema);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
