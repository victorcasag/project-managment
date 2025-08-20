package br.edu.infnet.victorapi.exceptions;

public class EntityInvalidException extends RuntimeException {

    private final String entityName;
    private final String field;
    private final String value;

    public EntityInvalidException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
        this.field = null;
        this.value = null;
    }

    public EntityInvalidException(String entityName, String message, Throwable cause) {
        super(message, cause);
        this.entityName = entityName;
        this.field = null;
        this.value = null;
    }

    public EntityInvalidException(String entityName, String field, String value) {
        super("Valor inválido para " + field + " em " + entityName + ": " + value);
        this.entityName = entityName;
        this.field = field;
        this.value = value;
    }

    public EntityInvalidException(String entityName, String field, String value, String reason) {
        super("Valor inválido para " + field + " em " + entityName + ": " + value + ". Motivo: " + reason);
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