package br.edu.infnet.victorapi.modules.projects.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Dados do projeto")
public class ProjectDTO {

    @Schema(description = "ID único do projeto", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Schema(description = "Nome do projeto", example = "Sistema de Vendas Online", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String name;

    @Schema(description = "Descrição detalhada do projeto", example = "Sistema completo para vendas online com integração de pagamento")
    private String description;

    @Schema(description = "ID do departamento responsável")
    private UUID departmentsId;

    @Schema(description = "Indica se o projeto é faturável", example = "true")
    private Boolean billableFl;

    @Schema(description = "Indica se é um projeto internacional", example = "false")
    private Boolean internationalFl;

    @Schema(description = "Data de criação do projeto")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização")
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getDepartmentsId() {
        return departmentsId;
    }

    public void setDepartmentsId(UUID departmentsId) {
        this.departmentsId = departmentsId;
    }

    public Boolean getBillableFl() {
        return billableFl;
    }

    public void setBillableFl(Boolean billableFl) {
        this.billableFl = billableFl;
    }

    public Boolean getInternationalFl() {
        return internationalFl;
    }

    public void setInternationalFl(Boolean internationalFl) {
        this.internationalFl = internationalFl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}