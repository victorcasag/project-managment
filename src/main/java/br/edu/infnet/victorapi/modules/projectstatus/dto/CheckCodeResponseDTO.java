package br.edu.infnet.victorapi.modules.projectstatus.dto;

public record CheckCodeResponseDTO(
        String code,
        boolean available,
        String message
) {}
