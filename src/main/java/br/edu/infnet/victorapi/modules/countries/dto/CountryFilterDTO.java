package br.edu.infnet.victorapi.modules.countries.dto;

public record CountryFilterDTO(
        String name,
        String code2,
        String code3,
        String currencyCode,
        Boolean isActive
) {
}
