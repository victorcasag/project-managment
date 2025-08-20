package br.edu.infnet.victorapi.modules.offices.dto;

public record OfficesFilterDTO(
        String name,
        String code,
        String city,
        String state,
        Integer countryId,
        String email,
        String phone,
        Boolean isMainOffice,
        Boolean isActive
) {
}
