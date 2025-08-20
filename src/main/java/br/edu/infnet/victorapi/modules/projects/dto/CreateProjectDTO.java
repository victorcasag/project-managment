package br.edu.infnet.victorapi.modules.projects.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProjectDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        @Schema(description = "Nome do projeto", example = "Novo Sistema de Vendas")
        String name,

        @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
        @Schema(description = "Descrição do projeto", example = "Sistema para automatização do processo de vendas")
        String description,

        @Schema(description = "ID do departamento", example = "1")
        Integer departmentsId,

        @NotNull(message = "Tipo de projeto é obrigatório")
        @Schema(description = "ID do tipo de projeto", example = "1")
        Integer projectTypesId,

        @Schema(description = "ID do setor", example = "1")
        Integer sectorsId,

        @Schema(description = "ID da área", example = "2")
        Integer areasId,

        @NotNull(message = "Status do projeto é obrigatório")
        @Schema(description = "ID do status do projeto", example = "1")
        Integer projectStatusesId,

        @Schema(description = "ID do projeto de origem", example = "1")
        Integer originProjectsId,

        @Schema(description = "ID do país", example = "1")
        Integer countriesId,

        @Schema(description = "ID do cliente/fornecedor", example = "2")
        Integer clientsSuppliersId,

        @Schema(description = "ID do último status do projeto", example = "1")
        Integer lastProjectStatusesId,

        @Schema(description = "ID do tipo de moeda", example = "1")
        Integer coinTypeId,

        @Schema(description = "ID da proposta de origem", example = "1")
        Integer originProposalId,

        @Schema(description = "Projeto faturável", example = "true")
        Boolean billableFl,

        @Schema(description = "Projeto internacional", example = "false")
        Boolean internationalFl,

        @Size(max = 255, message = "Diretório deve ter no máximo 255 caracteres")
        @Schema(description = "Diretório do projeto", example = "/projetos/vendas")
        String projectDir,

        @Size(max = 255, message = "Site deve ter no máximo 255 caracteres")
        @Schema(description = "Site do projeto", example = "vendas.empresa.com")
        String site,

        @Schema(description = "É projeto padrão", example = "false")
        Boolean isDefault,

        @Schema(description = "Taxa de câmbio", example = "1.0000")
        BigDecimal exchangeRate,

        @Size(max = 255, message = "Email deve ter no máximo 255 caracteres")
        @Schema(description = "Email de abertura", example = "vendas@empresa.com")
        String openingEmail,

        @Size(max = 100, message = "Classificação deve ter no máximo 100 caracteres")
        @Schema(description = "Classificação", example = "Projeto Estratégico")
        String classification,

        @Schema(description = "Projeto de investimento", example = "false")
        Boolean investimentFl,

        @Schema(description = "Projeto de produto", example = "true")
        Boolean productFl
) {}
