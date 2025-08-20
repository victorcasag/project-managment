package br.edu.infnet.victorapi.modules.proposals.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "proposals")
public class Proposals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "proposal_number")
    private String proposalNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "sector_id")
    private Integer sectorId;

    @Column(name = "area_id")
    private Integer areaId;

    @Column(name = "client_supplier_id")
    private Integer clientSupplierId;

    @Column(name = "contract_id")
    private Integer contractId;

    @Column(name = "office_id")
    private Integer officeId;

    @Column(name = "coin_type_id")
    private Integer coinTypeId;

    @Column(name = "country_id")
    private Integer countryId;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "responsible_id")
    private Integer responsibleId;

    @Column(name = "origin_proposal_id")
    private Integer originProposalId;

    @Column(name = "site")
    private String site;

    @Column(name = "value", precision = 15, scale = 2)
    private BigDecimal value;

    @Column(name = "schedule")
    private String schedule;

    @Column(name = "ibt", precision = 10, scale = 4)
    private BigDecimal ibt;

    @Column(name = "payment_days")
    private Integer paymentDays;

    @Column(name = "estimated_start")
    private LocalDate estimatedStart;

    @Column(name = "probability", precision = 5, scale = 2)
    private BigDecimal probability;

    @Column(name = "proposal_sub_number")
    private String proposalSubNumber;

    @Column(name = "exchange_rate", precision = 10, scale = 4)
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "priority")
    private Integer priority = 0;

    @Column(name = "due_days")
    private Integer dueDays;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.exchangeRate == null) {
            this.exchangeRate = BigDecimal.ONE;
        }
        if (this.priority == null) {
            this.priority = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProposalNumber() {
        return proposalNumber;
    }

    public void setProposalNumber(String proposalNumber) {
        this.proposalNumber = proposalNumber;
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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getSectorId() {
        return sectorId;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getClientSupplierId() {
        return clientSupplierId;
    }

    public void setClientSupplierId(Integer clientSupplierId) {
        this.clientSupplierId = clientSupplierId;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public Integer getCoinTypeId() {
        return coinTypeId;
    }

    public void setCoinTypeId(Integer coinTypeId) {
        this.coinTypeId = coinTypeId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(Integer responsibleId) {
        this.responsibleId = responsibleId;
    }

    public Integer getOriginProposalId() {
        return originProposalId;
    }

    public void setOriginProposalId(Integer originProposalId) {
        this.originProposalId = originProposalId;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public BigDecimal getIbt() {
        return ibt;
    }

    public void setIbt(BigDecimal ibt) {
        this.ibt = ibt;
    }

    public Integer getPaymentDays() {
        return paymentDays;
    }

    public void setPaymentDays(Integer paymentDays) {
        this.paymentDays = paymentDays;
    }

    public LocalDate getEstimatedStart() {
        return estimatedStart;
    }

    public void setEstimatedStart(LocalDate estimatedStart) {
        this.estimatedStart = estimatedStart;
    }

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public String getProposalSubNumber() {
        return proposalSubNumber;
    }

    public void setProposalSubNumber(String proposalSubNumber) {
        this.proposalSubNumber = proposalSubNumber;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getDueDays() {
        return dueDays;
    }

    public void setDueDays(Integer dueDays) {
        this.dueDays = dueDays;
    }
}