package com.mfinancas.api.exceptions;

public class FailedConditional extends RuntimeException {
    public FailedConditional(String message) {
        super(message);
    }
}
