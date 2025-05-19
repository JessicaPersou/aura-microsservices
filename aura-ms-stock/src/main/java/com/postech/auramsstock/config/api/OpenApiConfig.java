package com.postech.auramsstock.config.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.setExtensions(new HashMap<>());
        return new OpenAPI()
                .info(new Info()
                        .title("API Aura Microsservices - Stock")
                        .version("1.0.0")
                        .description("Descrição da API"));
    }
}