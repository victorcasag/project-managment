package br.edu.infnet.victorapi.modules.proposalstatus.dto;

public record CheckNameResponseDTO(
        String name,
        boolean available,
        String message
) {}
