package com.careernexus.common.exceptions;

public class ResourceNotFoundException extends CustomException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue),
                "RESOURCE_NOT_FOUND",
                404);
    }

    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND", 404);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id),
                "RESOURCE_NOT_FOUND",
                404);
    }
}