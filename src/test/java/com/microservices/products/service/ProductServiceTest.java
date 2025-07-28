package com.microservices.products.service;

import com.microservices.products.dto.ProductDto;
import com.microservices.products.entity.Product;
import com.microservices.products.exception.ProductNotFoundException;
import com.microservices.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductDto.CreateProductRequest createRequest;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setNombre("Producto Test");
        testProduct.setPrecio(new BigDecimal("99.99"));
        testProduct.setDescripcion("Descripción del producto test");

        createRequest = ProductDto.CreateProductRequest.builder()
                .nombre("Producto Test")
                .precio(new BigDecimal("99.99"))
                .descripcion("Descripción del producto test")
                .build();
    }

    @Test
    void createProduct_ShouldReturnProductDto_WhenValidRequest() {
        // Given
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        // When
        ProductDto result = productService.createProduct(createRequest);

        // Then
        assertNotNull(result);
        assertEquals("products", result.getType());
        assertEquals("1", result.getId());
        assertEquals("Producto Test", result.getAttributes().get("nombre"));
        assertEquals(new BigDecimal("99.99"), result.getAttributes().get("precio"));
        assertEquals("Descripción del producto test", result.getAttributes().get("descripcion"));

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductById_ShouldReturnProductDto_WhenProductExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        // When
        ProductDto result = productService.getProductById(1L);

        // Then
        assertNotNull(result);
        assertEquals("products", result.getType());
        assertEquals("1", result.getId());
        assertEquals("Producto Test", result.getAttributes().get("nombre"));

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_ShouldThrowException_WhenProductNotExists() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(1L)
        );

        assertEquals("Producto no encontrado con ID: 1", exception.getMessage());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        // Given
        Product product2 = new Product();
        product2.setId(2L);
        product2.setNombre("Producto 2");
        product2.setPrecio(new BigDecimal("149.99"));

        List<Product> products = Arrays.asList(testProduct, product2);
        when(productRepository.findAll()).thenReturn(products);

        // When
        List<ProductDto> result = productService.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Producto Test", result.get(0).getAttributes().get("nombre"));
        assertEquals("Producto 2", result.get(1).getAttributes().get("nombre"));

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void existsById_ShouldReturnTrue_WhenProductExists() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = productService.existsById(1L);

        // Then
        assertTrue(result);
        verify(productRepository, times(1)).existsById(1L);
    }

    @Test
    void existsById_ShouldReturnFalse_WhenProductNotExists() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(false);

        // When
        boolean result = productService.existsById(1L);

        // Then
        assertFalse(result);
        verify(productRepository, times(1)).existsById(1L);
    }
}