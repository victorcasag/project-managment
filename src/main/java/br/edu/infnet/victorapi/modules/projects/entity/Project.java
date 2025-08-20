package br.edu.infnet.victorapi.modules.projects.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "departments_id")
    private Integer departmentsId;

    @Column(name = "project_types_id")
    private Integer projectTypesId;

    @Column(name = "sectors_id")
    private Integer sectorsId;

    @Column(name = "areas_id")
    private Integer areasId;

    @Column(name = "project_statuses_id")
    private Integer projectStatusesId;

    @Column(name = "origin_projects_id")
    private Integer originProjectsId;

    @Column(name = "countries_id")
    private Integer countriesId;

    @Column(name = "clients_suppliers_id")
    private Integer clientsSuppliersId;

    @Column(name = "last_project_statuses_id")
    private Integer lastProjectStatusesId;

    @Column(name = "coin_type_id")
    private Integer coinTypeId;

    @Column(name = "origin_proposal_id")
    private Integer originProposalId;

    @Column(name = "billable_fl")
    private Boolean billableFl = false;

    @Column(name = "international_fl")
    private Boolean internationalFl = false;

    @Column(name = "project_dir")
    private String projectDir;

    @Column(name = "site")
    private String site;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "exchange_rate", precision = 15, scale = 4)
    private BigDecimal exchangeRate;

    @Column(name = "opening_email")
    private String openingEmail;

    @Column(name = "classification")
    private String classification;

    @Column(name = "investiment_fl")
    private Boolean investimentFl = false;

    @Column(name = "product_fl")
    private Boolean productFl = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.billableFl == null) {
            this.billableFl = false;
        }
        if (this.internationalFl == null) {
            this.internationalFl = false;
        }
        if (this.isDefault == null) {
            this.isDefault = false;
        }
        if (this.investimentFl == null) {
            this.investimentFl = false;
        }
        if (this.productFl == null) {
            this.productFl = false;
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

    public Integer getDepartmentsId() {
        return departmentsId;
    }

    public void setDepartmentsId(Integer departmentsId) {
        this.departmentsId = departmentsId;
    }

    public Integer getProjectTypesId() {
        return projectTypesId;
    }

    public void setProjectTypesId(Integer projectTypesId) {
        this.projectTypesId = projectTypesId;
    }

    public Integer getSectorsId() {
        return sectorsId;
    }

    public void setSectorsId(Integer sectorsId) {
        this.sectorsId = sectorsId;
    }

    public Integer getAreasId() {
        return areasId;
    }

    public void setAreasId(Integer areasId) {
        this.areasId = areasId;
    }

    public Integer getProjectStatusesId() {
        return projectStatusesId;
    }

    public void setProjectStatusesId(Integer projectStatusesId) {
        this.projectStatusesId = projectStatusesId;
    }

    public Integer getOriginProjectsId() {
        return originProjectsId;
    }

    public void setOriginProjectsId(Integer originProjectsId) {
        this.originProjectsId = originProjectsId;
    }

    public Integer getCountriesId() {
        return countriesId;
    }

    public void setCountriesId(Integer countriesId) {
        this.countriesId = countriesId;
    }

    public Integer getClientsSuppliersId() {
        return clientsSuppliersId;
    }

    public void setClientsSuppliersId(Integer clientsSuppliersId) {
        this.clientsSuppliersId = clientsSuppliersId;
    }

    public Integer getLastProjectStatusesId() {
        return lastProjectStatusesId;
    }

    public void setLastProjectStatusesId(Integer lastProjectStatusesId) {
        this.lastProjectStatusesId = lastProjectStatusesId;
    }

    public Integer getCoinTypeId() {
        return coinTypeId;
    }

    public void setCoinTypeId(Integer coinTypeId) {
        this.coinTypeId = coinTypeId;
    }

    public Integer getOriginProposalId() {
        return originProposalId;
    }

    public void setOriginProposalId(Integer originProposalId) {
        this.originProposalId = originProposalId;
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

    public String getProjectDir() {
        return projectDir;
    }

    public void setProjectDir(String projectDir) {
        this.projectDir = projectDir;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getOpeningEmail() {
        return openingEmail;
    }

    public void setOpeningEmail(String openingEmail) {
        this.openingEmail = openingEmail;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Boolean getInvestimentFl() {
        return investimentFl;
    }

    public void setInvestimentFl(Boolean investimentFl) {
        this.investimentFl = investimentFl;
    }

    public Boolean getProductFl() {
        return productFl;
    }

    public void setProductFl(Boolean productFl) {
        this.productFl = productFl;
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
