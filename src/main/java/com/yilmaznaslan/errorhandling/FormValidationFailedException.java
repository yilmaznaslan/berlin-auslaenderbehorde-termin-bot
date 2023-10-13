package com.yilmaznaslan.errorhandling;

public class FormValidationFailedException extends Exception {
    private static final long serialVersionUID = 1L;

    public FormValidationFailedException(String message) {
        super(message);
    }
}
