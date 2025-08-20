package br.edu.infnet.victorapi.modules.contract.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateContractDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do contrato", example = "Desenvolvimento Sistema ERP")
        String name,

        @Schema(description = "Descrição do contrato", example = "Contrato para desenvolvimento de sistema ERP completo")
        String description,

        @Size(max = 100, message = "Número do contrato deve ter no máximo 100 caracteres")
        @Schema(description = "Número do contrato", example = "CONT-2024-001")
        String contractNumber,

        @Schema(description = "Data de início", example = "2024-01-15")
        LocalDate startDate,

        @Schema(description = "Data de término", example = "2024-12-15")
        LocalDate endDate,

        @Positive(message = "Valor deve ser positivo")
        @Schema(description = "Valor do contrato", example = "250000.00")
        BigDecimal value,

        @Schema(description = "ID do tipo de moeda", example = "1")
        Integer coinTypeId,

        @Schema(description = "ID do cliente/fornecedor", example = "1")
        Integer clientSupplierId,

        @Schema(description = "Status ativo", example = "true")
        Boolean isActive
) {}
