package br.edu.infnet.victorapi.modules.countries.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCountryDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do país", example = "Argentina")
        String name,

        @Size(max = 2, message = "Código 2 deve ter no máximo 2 caracteres")
        @Schema(description = "Código ISO 2 dígitos", example = "AR")
        String code2,

        @Size(max = 3, message = "Código 3 deve ter no máximo 3 caracteres")
        @Schema(description = "Código ISO 3 dígitos", example = "ARG")
        String code3,

        @Size(max = 3, message = "Código da moeda deve ter no máximo 3 caracteres")
        @Schema(description = "Código da moeda", example = "ARS")
        String currencyCode
) {}
