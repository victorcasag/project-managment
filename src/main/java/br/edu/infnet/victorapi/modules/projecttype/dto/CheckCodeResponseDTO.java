package br.edu.infnet.victorapi.modules.projecttype.dto;

public record CheckCodeResponseDTO(
        String code,
        boolean available,
        String message
) {}
