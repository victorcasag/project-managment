package br.edu.infnet.victorapi.modules.clientsupplier.dto;

public record ClientSupplierFilterDTO(
        String name,
        String document,
        String documentType,
        String email,
        String phone,
        String city,
        String state,
        String type,
        Integer countryId,
        Boolean isActive
) {
}
