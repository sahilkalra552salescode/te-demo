package com.example.te_demo.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class JpaConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SchemaPerTenantConnectionProvider connectionProvider;

    @Autowired
    private TenantIdentifierResolver tenantResolver;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        LocalContainerEntityManagerFactoryBean em =
                new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.te_demo.entity");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>(
                jpaProperties.getProperties()
        );

        properties.put("hibernate.multiTenancy", "SCHEMA");
        properties.put(
                "hibernate.multi_tenant_connection_provider",
                connectionProvider
        );
        properties.put(
                "hibernate.tenant_identifier_resolver",
                tenantResolver
        );
        properties.put(
                "hibernate.dialect",
                "org.hibernate.dialect.PostgreSQLDialect"
        );

        em.setJpaPropertyMap(properties);
        return em;
    }
}
