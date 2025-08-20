package br.edu.infnet.victorapi.modules.projecttype.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectTypeDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do tipo de projeto", example = "API REST")
        String name,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
        @Schema(description = "Código do tipo de projeto", example = "API")
        String code,

        @Schema(description = "Descrição do tipo de projeto", example = "Desenvolvimento de APIs REST")
        String description
) {}
