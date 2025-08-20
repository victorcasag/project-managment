package br.edu.infnet.victorapi.modules.countries.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UpdateCountryDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do país", example = "Brasil")
        String name,

        @Size(max = 2, message = "Código 2 deve ter no máximo 2 caracteres")
        @Schema(description = "Código ISO 2 dígitos", example = "BR")
        String code2,

        @Size(max = 3, message = "Código 3 deve ter no máximo 3 caracteres")
        @Schema(description = "Código ISO 3 dígitos", example = "BRA")
        String code3,

        @Size(max = 3, message = "Código da moeda deve ter no máximo 3 caracteres")
        @Schema(description = "Código da moeda", example = "BRL")
        String currencyCode,

        @Schema(description = "Status ativo", example = "true")
        Boolean isActive
) {}
