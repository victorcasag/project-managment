package br.edu.infnet.victorapi.modules.area.dto;

public record AreaFilterDTO(
        String name,
        String code,
        String description,
        Boolean isActive
) {
}
