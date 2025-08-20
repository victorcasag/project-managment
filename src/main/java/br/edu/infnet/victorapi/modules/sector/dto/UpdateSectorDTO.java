package br.edu.infnet.victorapi.modules.sector.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UpdateSectorDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do setor", example = "Tecnologia")
        String name,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
        @Schema(description = "Código do setor", example = "TECH")
        String code,

        @Schema(description = "Descrição do setor", example = "Setor de tecnologia da informação")
        String description
) {}