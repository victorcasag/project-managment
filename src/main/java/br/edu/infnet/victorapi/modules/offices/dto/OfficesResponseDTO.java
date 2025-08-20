package br.edu.infnet.victorapi.modules.offices.dto;

import java.time.LocalDateTime;

public record OfficesResponseDTO(
        Integer id,
        String name,
        String code,
        String address,
        String city,
        String state,
        String postalCode,
        Integer countryId,
        String phone,
        String email,
        Boolean isMainOffice,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
