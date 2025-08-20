package br.edu.infnet.victorapi.modules.offices.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateOfficesDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do escritório", example = "Matriz")
        String name,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
        @Schema(description = "Código do escritório", example = "HQ")
        String code,

        @Size(max = 500, message = "Endereço deve ter no máximo 500 caracteres")
        @Schema(description = "Endereço completo", example = "Av. Paulista, 1000")
        String address,

        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        @Schema(description = "Cidade", example = "São Paulo")
        String city,

        @Size(max = 100, message = "Estado deve ter no máximo 100 caracteres")
        @Schema(description = "Estado", example = "SP")
        String state,

        @Size(max = 20, message = "CEP deve ter no máximo 20 caracteres")
        @Schema(description = "CEP", example = "01310-100")
        String postalCode,

        @Schema(description = "ID do país", example = "1")
        Integer countryId,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Schema(description = "Telefone", example = "(11) 3333-1111")
        String phone,

        @Email(message = "Email deve ter um formato válido")
        @Size(max = 255, message = "Email deve ter no máximo 255 caracteres")
        @Schema(description = "Email", example = "matriz@empresa.com")
        String email,

        @Schema(description = "É escritório principal", example = "true")
        Boolean isMainOffice,

        @Schema(description = "Status ativo", example = "true")
        Boolean isActive
) {}
