package com.microservices.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonApiResponse<T> {
    private T data;
    private List<JsonApiError> errors;
    private Meta meta;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JsonApiError {
        private String id;
        private String status;
        private String code;
        private String title;
        private String detail;
        private Map<String, Object> source;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Meta {
        private Integer count;
        private Integer page;
        private Integer pageSize;
        private Integer totalPages;
        private Long totalCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResourceObject {
        private String type;
        private String id;
        private Map<String, Object> attributes;
        private Map<String, Object> relationships;
    }

    public static <T> JsonApiResponse<T> success(T data) {
        return JsonApiResponse.<T>builder()
                .data(data)
                .build();
    }

    public static <T> JsonApiResponse<T> success(T data, Meta meta) {
        return JsonApiResponse.<T>builder()
                .data(data)
                .meta(meta)
                .build();
    }

    public static JsonApiResponse<Object> error(List<JsonApiError> errors) {
        return JsonApiResponse.builder()
                .errors(errors)
                .build();
    }
}