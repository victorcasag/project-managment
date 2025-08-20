package br.edu.infnet.victorapi.modules.projectstatus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProjectStatusDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        @Schema(description = "Nome do status", example = "Novo")
        String name,

        @NotBlank(message = "Código é obrigatório")
        @Size(max = 20, message = "Código deve ter no máximo 20 caracteres")
        @Schema(description = "Código do status", example = "NEW")
        String code,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        @Schema(description = "Descrição do status", example = "Projeto recém criado aguardando análise")
        String description,

        @Size(max = 7, message = "Cor deve ter no máximo 7 caracteres")
        @Schema(description = "Cor em hexadecimal", example = "#3B82F6")
        String color,

        @Schema(description = "Ordem de classificação", example = "1")
        Integer sortOrder,

        @Schema(description = "É status inicial", example = "true")
        Boolean isInitial,

        @Schema(description = "É status final", example = "false")
        Boolean isFinal
) {}
