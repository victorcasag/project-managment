package br.edu.infnet.victorapi.modules.countries.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record CountryResponseDTO(
        Integer id,
        String name,
        String code2,
        String code3,
        String currencyCode,
        Boolean isActive,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {}
