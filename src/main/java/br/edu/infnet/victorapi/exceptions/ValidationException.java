package br.edu.infnet.victorapi.exceptions;

import java.util.Map;
import java.util.HashMap;

public class ValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = new HashMap<>();
    }

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors != null ? fieldErrors : new HashMap<>();
    }

    public ValidationException(String field, String error) {
        super("Erro de validação no campo: " + field);
        this.fieldErrors = new HashMap<>();
        this.fieldErrors.put(field, error);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void addFieldError(String field, String error) {
        this.fieldErrors.put(field, error);
    }
}