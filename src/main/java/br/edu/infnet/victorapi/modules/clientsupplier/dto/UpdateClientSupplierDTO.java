package br.edu.infnet.victorapi.modules.clientsupplier.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateClientSupplierDTO(
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do cliente/fornecedor", example = "Tech Solutions Ltda")
        String name,

        @Size(max = 50, message = "Documento deve ter no máximo 50 caracteres")
        @Schema(description = "Documento", example = "12.345.678/0001-90")
        String document,

        @Size(max = 20, message = "Tipo de documento deve ter no máximo 20 caracteres")
        @Schema(description = "Tipo do documento", example = "CNPJ")
        String documentType,

        @Email(message = "Email deve ter um formato válido")
        @Size(max = 255, message = "Email deve ter no máximo 255 caracteres")
        @Schema(description = "Email", example = "contato@techsolutions.com.br")
        String email,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Schema(description = "Telefone", example = "(11) 3333-1111")
        String phone,

        @Size(max = 500, message = "Endereço deve ter no máximo 500 caracteres")
        @Schema(description = "Endereço", example = "Rua das Empresas, 123")
        String address,

        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        @Schema(description = "Cidade", example = "São Paulo")
        String city,

        @Size(max = 100, message = "Estado deve ter no máximo 100 caracteres")
        @Schema(description = "Estado", example = "SP")
        String state,

        @Size(max = 20, message = "CEP deve ter no máximo 20 caracteres")
        @Schema(description = "CEP", example = "01234-567")
        String postalCode,

        @Schema(description = "ID do país", example = "1")
        Integer countryId,

        @Pattern(regexp = "^(CLIENT|SUPPLIER|BOTH)$", message = "Tipo deve ser CLIENT, SUPPLIER ou BOTH")
        @Schema(description = "Tipo", example = "client", allowableValues = {"client", "supplier", "both"})
        String type,

        @Schema(description = "Status ativo", example = "true")
        Boolean isActive
) {}
