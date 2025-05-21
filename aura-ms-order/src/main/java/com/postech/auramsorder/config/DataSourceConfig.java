package com.postech.auramsorder.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "orderDataSource")
    @Primary
    public DataSource orderDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/AURA_MS_ORDER")
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "clientDataSource")
    public DataSource clientDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5434/aura_ms_client")
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "productDataSource")
    public DataSource productDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5433/aura_ms_product")
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "stockDataSource")
    public DataSource stockDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5435/aura_ms_stock")
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean(name = "paymentDataSource")
    public DataSource paymentDataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5436/aura_ms_payment")
                .username("postgres")
                .password("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}