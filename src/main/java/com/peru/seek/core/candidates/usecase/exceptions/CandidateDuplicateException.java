package com.peru.seek.core.candidates.usecase.exceptions;

public class CandidateDuplicateException extends RuntimeException {
    public CandidateDuplicateException(String message) {
        super(message);
    }
}
