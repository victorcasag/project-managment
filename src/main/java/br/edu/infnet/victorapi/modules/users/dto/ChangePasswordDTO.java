package br.edu.infnet.victorapi.modules.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ter formato válido")
        String email,

        @NotBlank(message = "Senha atual é obrigatória")
        String currentPassword,

        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 8, message = "Nova senha deve ter pelo menos 8 caracteres")
        String newPassword
) {}
