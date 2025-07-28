package com.microservices.products.controller;

import com.microservices.products.dto.JsonApiResponse;
import com.microservices.products.dto.ProductDto;
import com.microservices.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<JsonApiResponse<ProductDto>> createProduct(
            @Valid @RequestBody ProductDto.CreateProductRequest request) {

        log.info("=== POST /api/v1/products ===");
        log.info("Creando producto: {}", request.getNombre());

        try {
            ProductDto product = productService.createProduct(request);
            JsonApiResponse<ProductDto> response = JsonApiResponse.success(product);

            log.info("Producto creado exitosamente con ID: {}", product.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error al crear producto: ", e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<JsonApiResponse<List<ProductDto>>> getAllProducts() {
        log.info("=== GET /api/v1/products ===");

        try {
            List<ProductDto> products = productService.getAllProducts();
            JsonApiResponse.Meta meta = JsonApiResponse.Meta.builder()
                    .count(products.size())
                    .build();

            JsonApiResponse<List<ProductDto>> response = JsonApiResponse.success(products, meta);

            log.info("Retornando {} productos", products.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al listar productos: ", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonApiResponse<ProductDto>> getProduct(@PathVariable Long id) {
        log.info("=== GET /api/v1/products/{} ===", id);

        try {
            ProductDto product = productService.getProductById(id);
            JsonApiResponse<ProductDto> response = JsonApiResponse.success(product);

            log.info("Producto encontrado: {}", product.getAttributes().get("nombre"));
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener producto {}: ", id, e);
            throw e;
        }
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> productExists(@PathVariable Long id) {
        log.info("=== GET /api/v1/products/{}/exists ===", id);

        try {
            boolean exists = productService.existsById(id);
            log.info("Producto {} existe: {}", id, exists);
            return ResponseEntity.ok(exists);

        } catch (Exception e) {
            log.error("Error al verificar existencia {}: ", id, e);
            return ResponseEntity.ok(false);
        }
    }

    // Endpoint de diagnóstico
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("=== GET /api/v1/products/test ===");
        return ResponseEntity.ok("Controller funcionando correctamente!");
    }
}