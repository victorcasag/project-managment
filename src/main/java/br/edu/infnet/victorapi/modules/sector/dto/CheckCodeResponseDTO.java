package br.edu.infnet.victorapi.modules.sector.dto;

public record CheckCodeResponseDTO(
        String code,
        boolean available,
        String message
) {}