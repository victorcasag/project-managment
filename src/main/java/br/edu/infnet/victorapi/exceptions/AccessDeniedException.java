package br.edu.infnet.victorapi.exceptions;

public class AccessDeniedException extends RuntimeException {

    private final String resource;
    private final String action;
    private final String userId;

    public AccessDeniedException(String message) {
        super(message);
        this.resource = null;
        this.action = null;
        this.userId = null;
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
        this.resource = null;
        this.action = null;
        this.userId = null;
    }

    public AccessDeniedException(String resource, String action) {
        super("Acesso negado para " + action + " em " + resource);
        this.resource = resource;
        this.action = action;
        this.userId = null;
    }

    public AccessDeniedException(String resource, String action, String userId) {
        super("Usuário " + userId + " não tem permissão para " + action + " em " + resource);
        this.resource = resource;
        this.action = action;
        this.userId = userId;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }

    public String getUserId() {
        return userId;
    }
}