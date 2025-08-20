package br.edu.infnet.victorapi.modules.area.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UpdateAreaDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome da área", example = "Frontend")
        String name,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
        @Schema(description = "Código da área", example = "FRONTEND")
        String code,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        @Schema(description = "Descrição da área", example = "Área responsável pelo desenvolvimento de interfaces de usuário")
        String description,

        @Schema(description = "Status ativo", example = "true")
        Boolean isActive
) {}
