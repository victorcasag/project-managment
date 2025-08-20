package br.edu.infnet.victorapi.modules.users.dto;

public record CheckEmailResponseDTO(
        String email,
        boolean available,
        String message
) {}