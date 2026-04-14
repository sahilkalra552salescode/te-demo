package com.example.te_demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SchemaPerTenantConnectionProvider
        implements MultiTenantConnectionProvider<String> {

    private final DataSource defaultDataSource;
    private final Map<String, DataSource> tenantDataSources = new HashMap<>();

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public SchemaPerTenantConnectionProvider(
            DataSource defaultDataSource,
            @Value("${tenants.schemas}") List<String> tenants,
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) {

        this.defaultDataSource = defaultDataSource;
        this.url = url;
        this.username = username;
        this.password = password;

        for (String schema : tenants) {
            registerTenant(schema);
        }
    }

    public synchronized void registerTenant(String schema) {
        if (tenantDataSources.containsKey(schema)) return;
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setConnectionInitSql("SET search_path TO " + schema);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("pool-" + schema);
        tenantDataSources.put(schema, new HikariDataSource(config));
    }

    public boolean tenantExists(String schema) {
        return tenantDataSources.containsKey(schema);
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return defaultDataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        DataSource ds = tenantDataSources.get(tenantIdentifier);
        if (ds == null) {
            throw new SQLException("No datasource configured for tenant: " + tenantIdentifier);
        }
        return ds.getConnection();
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
}
