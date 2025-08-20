package br.edu.infnet.victorapi.modules.proposals.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProposalsFilterDTO(
        String name,
        String proposalNumber,
        Integer departmentId,
        Integer sectorId,
        Integer statusId,
        Integer responsibleId,
        BigDecimal minValue,
        BigDecimal maxValue,
        LocalDate startDate,
        LocalDate endDate,
        Integer priority
) {
}
