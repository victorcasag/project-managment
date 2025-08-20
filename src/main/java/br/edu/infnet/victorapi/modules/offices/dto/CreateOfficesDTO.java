package br.edu.infnet.victorapi.modules.offices.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOfficesDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do escritório", example = "Filial Belo Horizonte")
        String name,

        @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
        @Schema(description = "Código do escritório", example = "BH")
        String code,

        @Size(max = 500, message = "Endereço deve ter no máximo 500 caracteres")
        @Schema(description = "Endereço completo", example = "Av. Afonso Pena, 500")
        String address,

        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        @Schema(description = "Cidade", example = "Belo Horizonte")
        String city,

        @Size(max = 100, message = "Estado deve ter no máximo 100 caracteres")
        @Schema(description = "Estado", example = "MG")
        String state,

        @Size(max = 20, message = "CEP deve ter no máximo 20 caracteres")
        @Schema(description = "CEP", example = "30110-000")
        String postalCode,

        @Schema(description = "ID do país", example = "1")
        Integer countryId,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Schema(description = "Telefone", example = "(31) 3333-2222")
        String phone,

        @Email(message = "Email deve ter um formato válido")
        @Size(max = 255, message = "Email deve ter no máximo 255 caracteres")
        @Schema(description = "Email", example = "bh@empresa.com")
        String email,

        @Schema(description = "É escritório principal", example = "false")
        Boolean isMainOffice
) {}
