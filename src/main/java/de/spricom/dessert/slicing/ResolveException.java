package de.spricom.dessert.slicing;

public class ResolveException extends RuntimeException {
    public ResolveException(String message) {
        super(message);
    }

    public ResolveException(String message, Throwable cause) {
        super(message, cause);
    }
}
