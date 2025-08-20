package br.edu.infnet.victorapi.modules.departments.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDepartmentDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do departamento", example = "Qualidade")
        String name,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
        @Schema(description = "Código do departamento", example = "QA")
        String code,

        @Schema(description = "Descrição do departamento", example = "Departamento de garantia de qualidade")
        String description
) {}
