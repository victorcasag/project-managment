package br.edu.infnet.victorapi.modules.contract.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ContractResponseDTO(
        Integer id,
        String name,
        String description,
        String contractNumber,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        BigDecimal value,
        Integer coinTypeId,
        Integer clientSupplierId,
        Boolean isActive,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {}
