package com.microservices.products.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(getApiInfo())
                .servers(getServers())
                .components(getComponents())
                .addSecurityItem(new SecurityRequirement().addList("api-key"));
    }

    private Info getApiInfo() {
        return new Info()
                .title("Products Service API")
                .description("Microservicio para gestión de productos - Sistema de microservicios de productos e inventario")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Equipo de Desarrollo")
                        .email("dev@microservices.com")
                        .url("https://github.com/microservices/products-inventory"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("products-service")
                .pathsToMatch("/v1/**")
                .packagesToScan("com.microservices.products")
                .build();
    }

    private List<Server> getServers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort + "/api")
                        .description("Servidor de Desarrollo"),
                new Server()
                        .url("http://products-service:8081/api")
                        .description("Servidor Docker"),
                new Server()
                        .url("https://api-products.microservices.com/api")
                        .description("Servidor de Producción")
        );
    }

    private Components getComponents() {
        return new Components()
                .addSecuritySchemes("api-key", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("X-API-Key")
                        .description("API Key para autenticación entre microservicios"));
    }
}