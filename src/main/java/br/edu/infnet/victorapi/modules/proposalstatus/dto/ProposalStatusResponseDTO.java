package br.edu.infnet.victorapi.modules.proposalstatus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "name",
        "code",
        "description",
        "color",
        "sortOrder",
        "isActive",
        "isInitial",
        "isFinal",
        "createdAt",
        "updatedAt"
})
public class ProposalStatusResponseDTO {

    private Integer id;
    private String name;
    private String code;
    private String description;
    private String color;
    private Integer sortOrder;
    private Boolean isActive;
    private Boolean isInitial;
    private Boolean isFinal;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public ProposalStatusResponseDTO() {}

    public ProposalStatusResponseDTO(Integer id, String name, String code, String description,
                                   String color, Integer sortOrder, Boolean isActive, 
                                   Boolean isInitial, Boolean isFinal,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.color = color;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.isInitial = isInitial;
        this.isFinal = isFinal;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsInitial() {
        return isInitial;
    }

    public void setIsInitial(Boolean isInitial) {
        this.isInitial = isInitial;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
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
