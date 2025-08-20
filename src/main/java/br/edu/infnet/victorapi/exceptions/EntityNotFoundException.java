package br.edu.infnet.victorapi.exceptions;

public class EntityNotFoundException extends RuntimeException {

    private final String entityName;
    private final String field;
    private final String value;

    public EntityNotFoundException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
        this.field = null;
        this.value = null;
    }

    public EntityNotFoundException(String entityName, String message, Throwable cause) {
        super(message, cause);
        this.entityName = entityName;
        this.field = null;
        this.value = null;
    }

    public EntityNotFoundException(String entityName, Integer id) {
        super(entityName + " não encontrado com ID: " + id);
        this.entityName = entityName;
        this.field = "id";
        this.value = String.valueOf(id);
    }

    public EntityNotFoundException(String entityName, String field, String value) {
        super(entityName + " não encontrado com " + field + ": " + value);
        this.entityName = entityName;
        this.field = field;
        this.value = value;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}