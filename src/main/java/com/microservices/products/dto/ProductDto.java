package com.microservices.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta de producto siguiendo el estándar JSON API")
public class ProductDto {
    @Schema(description = "Tipo de recurso", example = "products", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type = "products";

    @Schema(description = "Identificador único del producto", example = "1")
    private String id;

    @Schema(description = "Atributos del producto")
    private Map<String, Object> attributes;

    // Request DTO para crear productos
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Datos requeridos para crear un nuevo producto")
    public static class CreateProductRequest {
        @NotBlank(message = "El nombre del producto es obligatorio")
        @Schema(
                description = "Nombre del producto",
                example = "Laptop Gaming",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minLength = 1,
                maxLength = 255
        )
        private String nombre;

        @NotNull(message = "El precio es obligatorio")
        @Positive(message = "El precio debe ser positivo")
        @Schema(
                description = "Precio del producto en USD",
                example = "1299.99",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "0.01"
        )
        private BigDecimal precio;

        @Schema(
                description = "Descripción detallada del producto (opcional)",
                example = "Laptop para gaming con tarjeta gráfica dedicada RTX 4060",
                maxLength = 500
        )
        private String descripcion;
    }

    public static ProductDto fromEntity(com.microservices.products.entity.Product product) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("nombre", product.getNombre());
        attributes.put("precio", product.getPrecio());
        attributes.put("descripcion", product.getDescripcion());

        return ProductDto.builder()
                .type("products")
                .id(product.getId().toString())
                .attributes(attributes)
                .build();
    }
}