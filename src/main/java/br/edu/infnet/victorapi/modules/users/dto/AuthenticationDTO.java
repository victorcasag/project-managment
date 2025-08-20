package br.edu.infnet.victorapi.modules.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ter formato válido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String password
) {}