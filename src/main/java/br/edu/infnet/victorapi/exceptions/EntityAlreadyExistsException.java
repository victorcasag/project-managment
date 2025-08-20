package br.edu.infnet.victorapi.exceptions;

public class EntityAlreadyExistsException extends RuntimeException {

    private final String entityName;
    private final String field;
    private final String value;

    public EntityAlreadyExistsException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
        this.field = null;
        this.value = null;
    }

    public EntityAlreadyExistsException(String entityName, String message, Throwable cause) {
        super(message, cause);
        this.entityName = entityName;
        this.field = null;
        this.value = null;
    }

    public EntityAlreadyExistsException(String entityName, String field, String value) {
        super("Já existe um " + entityName + " com " + field + ": " + value);
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
