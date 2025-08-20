package br.edu.infnet.victorapi.modules.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ter formato válido")
        @Size(max = 255, message = "Email deve ter no máximo 255 caracteres")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
        String password,

        @Size(max = 50, message = "Telefone deve ter no máximo 50 caracteres")
        String phone,

        Integer departmentId,

        @Size(max = 255, message = "Cargo deve ter no máximo 255 caracteres")
        String position,

        UserRole role
) {}
