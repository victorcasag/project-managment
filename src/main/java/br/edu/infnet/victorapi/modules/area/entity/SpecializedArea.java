package br.edu.infnet.victorapi.modules.area.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("SPECIALIZED")
public class SpecializedArea extends Area {

    @Column(name = "specialization_type")
    private String specializationType;

    @Column(name = "priority_level")
    private Integer priorityLevel;

    @Column(name = "requires_certification")
    private Boolean requiresCertification = false;

    @Column(name = "budget_limit")
    private Double budgetLimit;

    public SpecializedArea() {
        super();
    }

    public SpecializedArea(String name, String code, String description, 
                          String specializationType, Integer priorityLevel) {
        super(name, code, description);
        this.specializationType = specializationType;
        this.priorityLevel = priorityLevel;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && 
               specializationType != null && !specializationType.trim().isEmpty() &&
               priorityLevel != null && priorityLevel >= 1 && priorityLevel <= 5;
    }

    @Override
    protected void onPrePersist() {
        super.onPrePersist();
        
        if (this.specializationType != null) {
            this.specializationType = this.specializationType.toUpperCase().trim();
        }
        
        if (this.priorityLevel == null) {
            this.priorityLevel = 3;
        }
    }

    @Override
    public String getAuditInfo() {
        return String.format("SpecializedArea[id=%d, name='%s', code='%s', type='%s', priority=%d, " +
                           "certification=%s, budget=%.2f, active=%s, created=%s, updated=%s]",
            getId(), getName(), getCode(), specializationType, priorityLevel, 
            requiresCertification, budgetLimit, getIsActive(), getCreatedAt(), getUpdatedAt());
    }

    @Override
    public String getAuditIdentifier() {
        return "SPEC_AREA_" + (getId() != null ? getId() : "NEW") + "_" + 
               (getCode() != null ? getCode() : "NO_CODE") + "_" +
               (specializationType != null ? specializationType : "NO_TYPE");
    }

    @Override
    protected boolean canDeactivate() {
        return super.canDeactivate() && 
               (priorityLevel == null || priorityLevel < 5);
    }

    @Override
    protected void onDeactivate() {
        super.onDeactivate();
        System.out.println("Área especializada desativada - Tipo: " + specializationType + 
                          ", Prioridade: " + priorityLevel);
    }

    public boolean requiresHighLevelAccess() {
        return requiresCertification || (priorityLevel != null && priorityLevel >= 4);
    }

    public Double calculateOperationalCost() {
        if (budgetLimit == null || priorityLevel == null) {
            return 0.0;
        }
        
        double multiplier = switch (priorityLevel) {
            case 1 -> 0.5;
            case 2 -> 0.7;
            case 3 -> 1.0;
            case 4 -> 1.3;
            case 5 -> 1.5;
            default -> 1.0;
        };
        
        return budgetLimit * multiplier;
    }

    public String getSpecializationType() {
        return specializationType;
    }

    public void setSpecializationType(String specializationType) {
        this.specializationType = specializationType;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Boolean getRequiresCertification() {
        return requiresCertification;
    }

    public void setRequiresCertification(Boolean requiresCertification) {
        this.requiresCertification = requiresCertification;
    }

    public Double getBudgetLimit() {
        return budgetLimit;
    }

    public void setBudgetLimit(Double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }
}
