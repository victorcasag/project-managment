package br.edu.infnet.victorapi.modules.proposalstatus.dto;

public record CheckCodeResponseDTO(
        String code,
        boolean available,
        String message
) {}
