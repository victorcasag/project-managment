package br.edu.infnet.victorapi.exceptions;

public class EntityDeactivatedException extends RuntimeException {

    private final String entityName;
    private final String field;
    private final String value;

    public EntityDeactivatedException(String entityName, String message) {
        super(message);
        this.entityName = entityName;
        this.field = null;
        this.value = null;
    }

    public EntityDeactivatedException(String entityName, String message, Throwable cause) {
        super(message, cause);
        this.entityName = entityName;
        this.field = null;
        this.value = null;
    }

    public EntityDeactivatedException(String entityName, Integer id) {
        super("O " + entityName + " com ID " + id + " está desativado e não pode ser utilizado");
        this.entityName = entityName;
        this.field = "id";
        this.value = String.valueOf(id);
    }

    public EntityDeactivatedException(String entityName, String field, String value) {
        super("O " + entityName + " com " + field + " " + value + " está desativado e não pode ser utilizado");
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