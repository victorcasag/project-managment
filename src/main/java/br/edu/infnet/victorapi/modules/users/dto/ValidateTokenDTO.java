package br.edu.infnet.victorapi.modules.users.dto;

import jakarta.validation.constraints.NotBlank;

public record ValidateTokenDTO(
        @NotBlank(message = "Token é obrigatório")
        String token
) {}