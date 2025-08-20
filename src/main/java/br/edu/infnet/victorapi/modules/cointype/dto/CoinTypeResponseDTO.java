package br.edu.infnet.victorapi.modules.cointype.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record CoinTypeResponseDTO(
        Integer id,
        String name,
        String code,
        String symbol,
        Boolean isActive,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {}
