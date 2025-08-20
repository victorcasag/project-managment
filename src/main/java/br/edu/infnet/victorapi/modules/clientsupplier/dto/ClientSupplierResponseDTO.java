package br.edu.infnet.victorapi.modules.clientsupplier.dto;

import java.time.LocalDateTime;

public record ClientSupplierResponseDTO(
        Integer id,
        String name,
        String document,
        String documentType,
        String email,
        String phone,
        String address,
        String city,
        String state,
        String postalCode,
        Integer countryId,
        String type,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
