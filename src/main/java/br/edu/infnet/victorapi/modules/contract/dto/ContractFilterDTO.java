package br.edu.infnet.victorapi.modules.contract.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContractFilterDTO(
        String name,
        String contractNumber,
        String description,
        Integer clientSupplierId,
        Integer coinTypeId,
        LocalDate startDateFrom,
        LocalDate startDateTo,
        LocalDate endDateFrom,
        LocalDate endDateTo,
        BigDecimal valueFrom,
        BigDecimal valueTo,
        Boolean isActive
) {
}
