package com.yilmaznaslan.errorhandling;

public class FormValidationFailedException extends Exception {
    public FormValidationFailedException(String message) {
        super(message);
    }
}
