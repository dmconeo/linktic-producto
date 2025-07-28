package com.microservices.products.service;

import com.microservices.products.dto.ProductDto;
import com.microservices.products.entity.Product;
import com.microservices.products.exception.ProductNotFoundException;
import com.microservices.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductDto createProduct(ProductDto.CreateProductRequest request) {
        log.info("Creando nuevo producto: {}", request.getNombre());

        Product product = new Product();
        product.setNombre(request.getNombre());
        product.setPrecio(request.getPrecio());
        product.setDescripcion(request.getDescripcion());

        Product savedProduct = productRepository.save(product);
        log.info("Producto creado exitosamente con ID: {}", savedProduct.getId());

        return ProductDto.fromEntity(savedProduct);
    }

    public ProductDto getProductById(Long id) {
        log.info("Buscando producto con ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Producto no encontrado con ID: {}", id);
                    return new ProductNotFoundException("Producto no encontrado con ID: " + id);
                });

        return ProductDto.fromEntity(product);
    }

    public List<ProductDto> getAllProducts() {
        log.info("Obteniendo todos los productos");

        List<Product> products = productRepository.findAll();
        log.info("Se encontraron {} productos", products.size());

        return products.stream()
                .map(ProductDto::fromEntity)
                .collect(Collectors.toList());
    }

    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}