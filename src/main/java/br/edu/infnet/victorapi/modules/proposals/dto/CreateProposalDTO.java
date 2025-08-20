package br.edu.infnet.victorapi.modules.proposals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateProposalDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome da proposta", example = "Nova Proposta E-commerce")
        String name,

        @Schema(description = "Descrição da proposta", example = "Desenvolvimento de plataforma e-commerce moderna")
        String description,

        @Size(max = 100, message = "Número da proposta deve ter no máximo 100 caracteres")
        @Schema(description = "Número da proposta", example = "PROP-2025-001")
        String proposalNumber,

        @Schema(description = "ID do departamento", example = "1")
        Integer departmentId,
        
        @Schema(description = "ID do setor", example = "1")
        Integer sectorId,
        
        @Schema(description = "ID da área", example = "1")
        Integer areaId,
        
        @Schema(description = "ID do cliente/fornecedor", example = "2")
        Integer clientSupplierId,
        
        @Schema(description = "ID do contrato", example = "1")
        Integer contractId,
        
        @Schema(description = "ID do escritório", example = "1")
        Integer officeId,
        
        @Schema(description = "ID do tipo de moeda", example = "2")
        Integer coinTypeId,
        
        @Schema(description = "ID do país", example = "2")
        Integer countryId,
        
        @Schema(description = "ID do status", example = "1")
        Integer statusId,
        
        @Schema(description = "ID do responsável", example = "2")
        Integer responsibleId,
        
        @Schema(description = "ID da proposta de origem", example = "1")
        Integer originProposalId,

        @Size(max = 255, message = "Site deve ter no máximo 255 caracteres")
        @Schema(description = "Site", example = "www.novoecommerce.com")
        String site,

        @Positive(message = "Valor deve ser positivo")
        @Schema(description = "Valor da proposta", example = "250000.00")
        BigDecimal value,

        @Schema(description = "Cronograma", example = "Desenvolvimento em 8 meses")
        String schedule,

        @DecimalMin(value = "0.0", message = "IBT deve ser maior ou igual a 0")
        @Schema(description = "IBT", example = "20000.00")
        BigDecimal ibt,

        @Positive(message = "Dias de pagamento deve ser positivo")
        @Schema(description = "Dias de pagamento", example = "45")
        Integer paymentDays,

        @Schema(description = "Data estimada de início", example = "2025-02-01")
        LocalDate estimatedStart,

        @DecimalMin(value = "0.0", message = "Probabilidade deve ser entre 0 e 100")
        @DecimalMax(value = "100.0", message = "Probabilidade deve ser entre 0 e 100")
        @Schema(description = "Probabilidade de fechamento", example = "85.00")
        BigDecimal probability,

        @Size(max = 50, message = "Sub-número da proposta deve ter no máximo 50 caracteres")
        @Schema(description = "Sub-número da proposta", example = "001-B")
        String proposalSubNumber,

        @Positive(message = "Taxa de câmbio deve ser positiva")
        @Schema(description = "Taxa de câmbio", example = "5.2500")
        BigDecimal exchangeRate,

        @Size(max = 255, message = "Nome da empresa deve ter no máximo 255 caracteres")
        @Schema(description = "Nome da empresa", example = "Nova Empresa Digital")
        String companyName,

        @Schema(description = "Prioridade", example = "1")
        Integer priority,

        @Positive(message = "Dias de vencimento deve ser positivo")
        @Schema(description = "Dias de vencimento", example = "90")
        Integer dueDays
) {}
