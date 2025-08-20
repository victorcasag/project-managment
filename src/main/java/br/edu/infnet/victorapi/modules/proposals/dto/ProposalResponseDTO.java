package br.edu.infnet.victorapi.modules.proposals.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "proposalNumber",
        "departmentId",
        "departmentName",
        "sectorId",
        "sectorName",
        "areaId",
        "areaName",
        "clientSupplierId",
        "clientSupplierName",
        "contractId",
        "contractName",
        "officeId",
        "officeName",
        "coinTypeId",
        "coinTypeName",
        "countryId",
        "countryName",
        "statusId",
        "statusName",
        "responsibleId",
        "responsibleName",
        "originProposalId",
        "site",
        "value",
        "schedule",
        "ibt",
        "paymentDays",
        "estimatedStart",
        "probability",
        "proposalSubNumber",
        "exchangeRate",
        "companyName",
        "priority",
        "dueDays",
        "createdAt",
        "updatedAt"
})
public class ProposalResponseDTO {

    private Integer id;
    private String name;
    private String description;
    private String proposalNumber;
    
    // IDs dos relacionamentos
    private Integer departmentId;
    private Integer sectorId;
    private Integer areaId;
    private Integer clientSupplierId;
    private Integer contractId;
    private Integer officeId;
    private Integer coinTypeId;
    private Integer countryId;
    private Integer statusId;
    private Integer responsibleId;
    private Integer originProposalId;
    
    // Nomes dos relacionamentos
    private String departmentName;
    private String sectorName;
    private String areaName;
    private String clientSupplierName;
    private String contractName;
    private String officeName;
    private String coinTypeName;
    private String countryName;
    private String statusName;
    private String responsibleName;
    
    private String site;
    private BigDecimal value;
    private String schedule;
    private BigDecimal ibt;
    private Integer paymentDays;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate estimatedStart;
    
    private BigDecimal probability;
    private String proposalSubNumber;
    private BigDecimal exchangeRate;
    private String companyName;
    private Integer priority;
    private Integer dueDays;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public ProposalResponseDTO() {}

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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getSectorId() {
        return sectorId;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getClientSupplierId() {
        return clientSupplierId;
    }

    public void setClientSupplierId(Integer clientSupplierId) {
        this.clientSupplierId = clientSupplierId;
    }

    public String getClientSupplierName() {
        return clientSupplierName;
    }

    public void setClientSupplierName(String clientSupplierName) {
        this.clientSupplierName = clientSupplierName;
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Integer officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public Integer getCoinTypeId() {
        return coinTypeId;
    }

    public void setCoinTypeId(Integer coinTypeId) {
        this.coinTypeId = coinTypeId;
    }

    public String getCoinTypeName() {
        return coinTypeName;
    }

    public void setCoinTypeName(String coinTypeName) {
        this.coinTypeName = coinTypeName;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(Integer responsibleId) {
        this.responsibleId = responsibleId;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
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
