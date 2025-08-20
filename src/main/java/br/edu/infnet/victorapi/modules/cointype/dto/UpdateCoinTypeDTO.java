package br.edu.infnet.victorapi.modules.cointype.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UpdateCoinTypeDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome da moeda", example = "Real Brasileiro")
        String name,

        @Size(max = 3, message = "Código deve ter no máximo 3 caracteres")
        @Schema(description = "Código da moeda", example = "BRL")
        String code,

        @Size(max = 10, message = "Símbolo deve ter no máximo 10 caracteres")
        @Schema(description = "Símbolo da moeda", example = "R$")
        String symbol,

        @Schema(description = "Status ativo", example = "true")
        Boolean isActive
) {}
