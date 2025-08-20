package br.edu.infnet.victorapi.modules.area.dto;

import java.time.LocalDateTime;

public record AreaResponseDTO(
        Integer id,
        String name,
        String code,
        String description,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
