package br.edu.infnet.victorapi.config.security;

import br.edu.infnet.victorapi.modules.users.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public String getCurrentUserEmail() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    public User getCurrentUser() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    public Integer getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public String getCurrentUserName() {
        User user = getCurrentUser();
        return user != null ? user.getName() : null;
    }

    public String getCurrentUserRole() {
        User user = getCurrentUser();
        return user != null ? user.getRole().toString() : null;
    }

    public boolean isAuthenticated() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String name = authentication.getName();
        if (name == null || name.equals("anonymousUser")) {
            return false;
        }

        return true;
    }

    public boolean isNotAuthenticated() {
        return !isAuthenticated();
    }

    public boolean hasRole(String role) {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals(role));
        }
        return false;
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public boolean isManager() {
        return hasRole("ROLE_MANAGER");
    }

    public boolean canAccessUser(Integer targetUserId) {
        if (isAdmin()) {
            return true;
        }

        Integer currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(targetUserId);
    }

    public boolean canAccessUserByEmail(String targetEmail) {
        if (isAdmin()) {
            return true;
        }

        String currentEmail = getCurrentUserEmail();
        return currentEmail != null && currentEmail.equals(targetEmail);
    }

    public String getSecurityContextInfo() {
        var context = SecurityContextHolder.getContext();
        var authentication = context.getAuthentication();

        if (authentication == null) {
            return "Authentication: null";
        }

        StringBuilder info = new StringBuilder();
        info.append("Authenticated: ").append(authentication.isAuthenticated()).append("\n");
        info.append("Principal: ").append(authentication.getPrincipal().getClass().getSimpleName()).append("\n");
        info.append("Name: ").append(authentication.getName()).append("\n");
        info.append("Authorities: ").append(authentication.getAuthorities()).append("\n");

        return info.toString();
    }

    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }
}