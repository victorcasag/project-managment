package br.edu.infnet.victorapi.modules.sector.dto;

public record CheckNameResponseDTO(
        String name,
        boolean available,
        String message
) {}