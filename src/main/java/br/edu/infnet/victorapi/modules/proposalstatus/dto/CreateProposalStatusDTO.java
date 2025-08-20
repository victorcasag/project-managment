package br.edu.infnet.victorapi.modules.proposalstatus.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProposalStatusDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do status", example = "Aguardando Resposta")
        String name,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
        @Schema(description = "Código do status", example = "WAITING_RESPONSE")
        String code,

        @Schema(description = "Descrição do status", example = "Proposta enviada, aguardando resposta do cliente")
        String description,
        
        @Size(max = 7, message = "Cor deve ter no máximo 7 caracteres")
        @Schema(description = "Cor em hexadecimal", example = "#FFA500")
        String color,
        
        @Schema(description = "Ordem de classificação", example = "5")
        Integer sortOrder,
        
        @Schema(description = "É status inicial", example = "false")
        Boolean isInitial,
        
        @Schema(description = "É status final", example = "false")
        Boolean isFinal
) {}
