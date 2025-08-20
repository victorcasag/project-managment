package br.edu.infnet.victorapi.modules.projects.dto;

public record CheckCodeResponseDTO(
        String code,
        boolean available,
        String message
) {}
