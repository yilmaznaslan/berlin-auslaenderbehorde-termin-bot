package org.example.exceptions;

public class FormValidationFailed extends Exception {
    public FormValidationFailed(String message) {
        super(message);
    }
}
