package br.edu.infnet.victorapi.modules.projecttype.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UpdateProjectTypeDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do tipo de projeto", example = "Desenvolvimento de Software")
        String name,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
        @Schema(description = "Código do tipo de projeto", example = "SOFTWARE")
        String code,

        @Schema(description = "Descrição do tipo de projeto", example = "Projetos de desenvolvimento de software completo")
        String description
) {}
