package br.edu.infnet.victorapi.modules.users.dto;

public enum UserRole {
    ROLE_ADMIN("ROLE_ADMIN", "Administrador"),
    ROLE_USER("ROLE_USER", "Usuário"),
    ROLE_MANAGER("ROLE_MANAGER", "Gerente"),
    ROLE_DEVELOPER("ROLE_DEVELOPER", "Desenvolvedor"),
    ROLE_ANALYST("ROLE_ANALYST", "Analista");

    private final String role;
    private final String description;

    UserRole(String role, String description) {
        this.role = role;
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.role;
    }

    public static UserRole fromString(String role) {
        for (UserRole userRole : UserRole.values()) {
            if (userRole.role.equalsIgnoreCase(role)) {
                return userRole;
            }
        }
        throw new IllegalArgumentException("Role não encontrada: " + role);
    }
}