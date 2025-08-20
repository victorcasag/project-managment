package br.edu.infnet.victorapi.exceptions;

public class BusinessOperationException extends RuntimeException {

    private final String operation;
    private final String entityName;
    private final String entityId;

    public BusinessOperationException(String message) {
        super(message);
        this.operation = null;
        this.entityName = null;
        this.entityId = null;
    }

    public BusinessOperationException(String message, Throwable cause) {
        super(message, cause);
        this.operation = null;
        this.entityName = null;
        this.entityId = null;
    }

    public BusinessOperationException(String operation, String reason) {
        super("Não foi possível executar a operação '" + operation + "'. Motivo: " + reason);
        this.operation = operation;
        this.entityName = null;
        this.entityId = null;
    }

    public BusinessOperationException(String operation, String entityName, String entityId, String reason) {
        super("Não foi possível executar a operação '" + operation + "' em " + entityName + " ID " + entityId + ". Motivo: " + reason);
        this.operation = operation;
        this.entityName = entityName;
        this.entityId = entityId;
    }

    public String getOperation() {
        return operation;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getEntityId() {
        return entityId;
    }
}