package com.textbasedgame.common;


public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("Resource of type " + resourceType + " with ID " + resourceId + " not found");
    }
    public ResourceNotFoundException(String customMessage) {
        super(customMessage);
    }

}