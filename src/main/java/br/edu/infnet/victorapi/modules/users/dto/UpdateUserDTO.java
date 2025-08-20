package br.edu.infnet.victorapi.modules.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UpdateUserDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do usuário", example = "Maria Silva")
        String name,

        @Size(max = 50, message = "Telefone deve ter no máximo 50 caracteres")
        @Schema(description = "Telefone", example = "(11) 99999-5555")
        String phone,

        @Size(max = 255, message = "Cargo deve ter no máximo 255 caracteres")
        @Schema(description = "Cargo", example = "Gerente de Projetos")
        String position,

        @Schema(description = "ID do departamento", example = "1")
        Integer departmentId,

        @Schema(description = "Papel do usuário", example = "USER")
        UserRole role
) {}