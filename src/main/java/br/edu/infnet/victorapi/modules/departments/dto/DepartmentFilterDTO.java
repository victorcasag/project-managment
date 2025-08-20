package br.edu.infnet.victorapi.modules.departments.dto;

public record DepartmentFilterDTO(
        String name,
        String code,
        String description,
        Boolean isActive
) {
}
