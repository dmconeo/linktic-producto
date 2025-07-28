package com.microservices.products.exception;

import com.microservices.products.dto.JsonApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<JsonApiResponse<Object>> handleProductNotFound(ProductNotFoundException ex) {
        log.error("Producto no encontrado: {}", ex.getMessage());

        JsonApiResponse.JsonApiError error = JsonApiResponse.JsonApiError.builder()
                .status("404")
                .code("PRODUCT_NOT_FOUND")
                .title("Producto no encontrado")
                .detail(ex.getMessage())
                .build();

        JsonApiResponse<Object> response = JsonApiResponse.error(List.of(error));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error("Error de validación: {}", ex.getMessage());

        List<JsonApiResponse.JsonApiError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::createValidationError)
                .collect(Collectors.toList());

        JsonApiResponse<Object> response = JsonApiResponse.error(errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonApiResponse<Object>> handleGenericException(Exception ex) {
        log.error("Error interno del servidor: {}", ex.getMessage(), ex);

        JsonApiResponse.JsonApiError error = JsonApiResponse.JsonApiError.builder()
                .status("500")
                .code("INTERNAL_SERVER_ERROR")
                .title("Error interno del servidor")
                .detail("Ha ocurrido un error inesperado")
                .build();

        JsonApiResponse<Object> response = JsonApiResponse.error(List.of(error));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private JsonApiResponse.JsonApiError createValidationError(FieldError fieldError) {
        return JsonApiResponse.JsonApiError.builder()
                .status("400")
                .code("VALIDATION_ERROR")
                .title("Error de validación")
                .detail(fieldError.getDefaultMessage())
                .source(Map.of("pointer", "/data/attributes/" + fieldError.getField()))
                .build();
    }
}