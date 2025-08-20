package br.edu.infnet.victorapi.modules.departments.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UpdateDepartmentDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do departamento", example = "Desenvolvimento")
        String name,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
        @Schema(description = "Código do departamento", example = "DEV")
        String code,

        @Schema(description = "Descrição do departamento", example = "Departamento responsável pelo desenvolvimento de software")
        String description,

        @Schema(description = "Status ativo", example = "true")
        Boolean isActive
) {}
