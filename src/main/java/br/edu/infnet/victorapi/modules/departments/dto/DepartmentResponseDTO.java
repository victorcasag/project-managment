package br.edu.infnet.victorapi.modules.departments.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record DepartmentResponseDTO(
        Integer id,
        String name,
        String code,
        String description,
        Boolean isActive,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {}
