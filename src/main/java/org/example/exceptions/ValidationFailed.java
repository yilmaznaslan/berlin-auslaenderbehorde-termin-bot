package org.example.exceptions;

public class ValidationFailed extends Exception {
    public ValidationFailed(String message) {
        super(message);
    }
}
