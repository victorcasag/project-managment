package br.edu.infnet.victorapi.modules.proposals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateProposalDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome da proposta", example = "Proposta Sistema CRM")
        String name,

        @Schema(description = "Descrição da proposta", example = "Desenvolvimento de sistema de gestão de relacionamento com cliente")
        String description,

        @Size(max = 100, message = "Número da proposta deve ter no máximo 100 caracteres")
        @Schema(description = "Número da proposta", example = "PROP-2024-001")
        String proposalNumber,

        @Schema(description = "ID do departamento", example = "1")
        Integer departmentId,
        
        @Schema(description = "ID do setor", example = "1")
        Integer sectorId,
        
        @Schema(description = "ID da área", example = "2")
        Integer areaId,
        
        @Schema(description = "ID do cliente/fornecedor", example = "1")
        Integer clientSupplierId,
        
        @Schema(description = "ID do contrato", example = "1")
        Integer contractId,
        
        @Schema(description = "ID do escritório", example = "1")
        Integer officeId,
        
        @Schema(description = "ID do tipo de moeda", example = "1")
        Integer coinTypeId,
        
        @Schema(description = "ID do país", example = "1")
        Integer countryId,
        
        @Schema(description = "ID do status", example = "2")
        Integer statusId,
        
        @Schema(description = "ID do responsável", example = "1")
        Integer responsibleId,
        
        @Schema(description = "ID da proposta de origem", example = "1")
        Integer originProposalId,

        @Size(max = 255, message = "Site deve ter no máximo 255 caracteres")
        @Schema(description = "Site", example = "www.crmsystem.com.br")
        String site,

        @Positive(message = "Valor deve ser positivo")
        @Schema(description = "Valor da proposta", example = "180000.00")
        BigDecimal value,

        @Schema(description = "Cronograma", example = "Desenvolvimento em 6 meses")
        String schedule,

        @DecimalMin(value = "0.0", message = "IBT deve ser maior ou igual a 0")
        @Schema(description = "IBT", example = "15000.00")
        BigDecimal ibt,

        @Positive(message = "Dias de pagamento deve ser positivo")
        @Schema(description = "Dias de pagamento", example = "30")
        Integer paymentDays,

        @Schema(description = "Data estimada de início", example = "2024-09-01")
        LocalDate estimatedStart,

        @DecimalMin(value = "0.0", message = "Probabilidade deve ser entre 0 e 100")
        @DecimalMax(value = "100.0", message = "Probabilidade deve ser entre 0 e 100")
        @Schema(description = "Probabilidade de fechamento", example = "75.00")
        BigDecimal probability,

        @Size(max = 50, message = "Sub-número da proposta deve ter no máximo 50 caracteres")
        @Schema(description = "Sub-número da proposta", example = "001-A")
        String proposalSubNumber,

        @Positive(message = "Taxa de câmbio deve ser positiva")
        @Schema(description = "Taxa de câmbio", example = "1.0000")
        BigDecimal exchangeRate,

        @Size(max = 255, message = "Nome da empresa deve ter no máximo 255 caracteres")
        @Schema(description = "Nome da empresa", example = "Tech Solutions Ltda")
        String companyName,

        @Schema(description = "Prioridade", example = "1")
        Integer priority,

        @Positive(message = "Dias de vencimento deve ser positivo")
        @Schema(description = "Dias de vencimento", example = "60")
        Integer dueDays
) {}
