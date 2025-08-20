package br.edu.infnet.victorapi.modules.proposals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record ConvertProposalToProjectDTO(
        @Size(max = 255, message = "Nome do projeto deve ter no máximo 255 caracteres")
        @Schema(description = "Nome do projeto (se não fornecido, usará o nome da proposta)", 
                example = "Projeto Sistema ERP - TechCorp")
        String projectName,

        @Schema(description = "ID do tipo de projeto", example = "1")
        Integer projectTypeId,

        @Schema(description = "Descrição adicional do projeto", 
                example = "Projeto convertido da proposta aprovada")
        String additionalDescription,

        @Schema(description = "Diretório do projeto", example = "/projetos/techcorp-erp")
        String projectDirectory,

        @Schema(description = "E-mail de abertura do projeto", 
                example = "projeto.erp@empresa.com")
        String openingEmail,

        @Schema(description = "Classificação do projeto", example = "ALTO")
        String classification,

        @Schema(description = "Projeto faturável", example = "true")
        Boolean billable,

        @Schema(description = "Projeto internacional", example = "false")
        Boolean international,

        @Schema(description = "Projeto de investimento", example = "false")
        Boolean investment,

        @Schema(description = "Projeto de produto", example = "true")
        Boolean product,

        @Schema(description = "ID do status inicial do projeto (se não fornecido, usará status padrão)", 
                example = "1")
        Integer initialProjectStatusId
) {}
