package com.bakery.inventory.exception;

/**
 * Thrown when attempting to delete or modify a resource that is still
 * referenced by other resources (e.g. a unit still used by products).
 */
public class ResourceInUseException extends RuntimeException {

    public ResourceInUseException(String message) {
        super(message);
    }
}
