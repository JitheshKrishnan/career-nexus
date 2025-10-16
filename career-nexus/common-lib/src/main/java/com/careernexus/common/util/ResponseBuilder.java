package com.careernexus.common.util;

import com.careernexus.common.dto.ApiResponse;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {

    private ResponseBuilder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Build a success response with data
     */
    public static <T> ApiResponse<T> buildSuccessResponse(T data) {
        return ApiResponse.success(data);
    }

    /**
     * Build a success response with custom message and data
     */
    public static <T> ApiResponse<T> buildSuccessResponse(String message, T data) {
        return ApiResponse.success(message, data);
    }

    /**
     * Build a success response with message, data, and metadata
     */
    public static <T> ApiResponse<T> buildSuccessResponse(String message, T data, Map<String, Object> metadata) {
        ApiResponse<T> response = ApiResponse.success(message, data);
        if (metadata != null) {
            metadata.forEach(response::addMetadata);
        }
        return response;
    }

    /**
     * Build an error response with message
     */
    public static <T> ApiResponse<T> buildErrorResponse(String message) {
        return ApiResponse.error(message);
    }

    /**
     * Build an error response with message and error details
     */
    public static <T> ApiResponse<T> buildErrorResponse(String message, T errorDetails) {
        return ApiResponse.error(message, errorDetails);
    }

    /**
     * Build an error response with message, error details, and metadata
     */
    public static <T> ApiResponse<T> buildErrorResponse(String message, T errorDetails, Map<String, Object> metadata) {
        ApiResponse<T> response = ApiResponse.error(message, errorDetails);
        if (metadata != null) {
            metadata.forEach(response::addMetadata);
        }
        return response;
    }

    /**
     * Build a paginated success response
     */
    public static <T> ApiResponse<T> buildPaginatedResponse(T data, int page, int size, long totalElements, int totalPages) {
        ApiResponse<T> response = ApiResponse.success("Data retrieved successfully", data);

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", page);
        pagination.put("size", size);
        pagination.put("totalElements", totalElements);
        pagination.put("totalPages", totalPages);

        response.addMetadata("pagination", pagination);
        return response;
    }

    /**
     * Build a response for created resource
     */
    public static <T> ApiResponse<T> buildCreatedResponse(T data) {
        return ApiResponse.success("Resource created successfully", data);
    }

    /**
     * Build a response for updated resource
     */
    public static <T> ApiResponse<T> buildUpdatedResponse(T data) {
        return ApiResponse.success("Resource updated successfully", data);
    }

    /**
     * Build a response for deleted resource
     */
    public static ApiResponse<Void> buildDeletedResponse() {
        return ApiResponse.success("Resource deleted successfully", null);
    }

    /**
     * Build a no content response
     */
    public static ApiResponse<Void> buildNoContentResponse() {
        return ApiResponse.success("No content", null);
    }

    /**
     * Build a validation error response
     */
    public static ApiResponse<Map<String, String>> buildValidationErrorResponse(Map<String, String> errors) {
        return ApiResponse.error("Validation failed", errors);
    }

    /**
     * Build an unauthorized response
     */
    public static ApiResponse<Void> buildUnauthorizedResponse() {
        return ApiResponse.error("Unauthorized access");
    }

    /**
     * Build a forbidden response
     */
    public static ApiResponse<Void> buildForbiddenResponse() {
        return ApiResponse.error("Access forbidden");
    }
}