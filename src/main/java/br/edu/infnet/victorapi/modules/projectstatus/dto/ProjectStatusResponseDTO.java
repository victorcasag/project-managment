package br.edu.infnet.victorapi.modules.projectstatus.dto;

import java.time.LocalDateTime;

public record ProjectStatusResponseDTO(
        Integer id,
        String name,
        String code,
        String description,
        String color,
        Integer sortOrder,
        Boolean isActive,
        Boolean isInitial,
        Boolean isFinal,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
