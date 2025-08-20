package br.edu.infnet.victorapi.modules.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateContractDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do contrato", example = "Novo Contrato de Desenvolvimento")
        String name,

        @Schema(description = "Descrição do contrato", example = "Contrato para desenvolvimento de nova funcionalidade")
        String description,

        @Size(max = 100, message = "Número do contrato deve ter no máximo 100 caracteres")
        @Schema(description = "Número do contrato", example = "CONT-2025-001")
        String contractNumber,

        @Schema(description = "Data de início", example = "2025-01-01")
        LocalDate startDate,

        @Schema(description = "Data de término", example = "2025-06-30")
        LocalDate endDate,

        @Positive(message = "Valor deve ser positivo")
        @Schema(description = "Valor do contrato", example = "150000.00")
        BigDecimal value,

        @Schema(description = "ID do tipo de moeda", example = "1")
        Integer coinTypeId,

        @Schema(description = "ID do cliente/fornecedor", example = "2")
        Integer clientSupplierId
) {}
