package br.edu.infnet.victorapi.modules.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do usuário", example = "Lucas Fernandes")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ter formato válido")
        @Size(max = 255, message = "Email deve ter no máximo 255 caracteres")
        @Schema(description = "Email do usuário", example = "lucas.fernandes@empresa.com")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
        @Schema(description = "Senha do usuário", example = "minhasenha123")
        String password,

        @Size(max = 50, message = "Telefone deve ter no máximo 50 caracteres")
        @Schema(description = "Telefone", example = "(11) 99999-7777")
        String phone,

        @Schema(description = "ID do departamento", example = "2")
        Integer departmentId,

        @Size(max = 255, message = "Cargo deve ter no máximo 255 caracteres")
        @Schema(description = "Cargo", example = "Analista de Marketing")
        String position,

        @Schema(description = "Papel do usuário", example = "USER")
        UserRole role
) {}
