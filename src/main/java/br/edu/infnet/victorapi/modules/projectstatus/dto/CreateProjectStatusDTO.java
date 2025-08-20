package br.edu.infnet.victorapi.modules.projectstatus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectStatusDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        @Schema(description = "Nome do status", example = "Em Homologação")
        String name,

        @NotBlank(message = "Código é obrigatório")
        @Size(max = 20, message = "Código deve ter no máximo 20 caracteres")
        @Schema(description = "Código do status", example = "HOMOLOG")
        String code,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        @Schema(description = "Descrição do status", example = "Projeto em fase de homologação com cliente")
        String description,

        @Size(max = 7, message = "Cor deve ter no máximo 7 caracteres")
        @Schema(description = "Cor em hexadecimal", example = "#FFA500")
        String color,

        @Schema(description = "Ordem de classificação", example = "7")
        Integer sortOrder,

        @Schema(description = "É status inicial", example = "false")
        Boolean isInitial,

        @Schema(description = "É status final", example = "false")
        Boolean isFinal
) {}
