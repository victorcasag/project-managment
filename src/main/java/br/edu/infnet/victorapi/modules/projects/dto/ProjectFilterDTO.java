package br.edu.infnet.victorapi.modules.projects.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProjectFilterDTO(
        String name,
        String description,
        String code,
        Integer departmentsId,
        Integer projectTypesId,
        Integer projectTypeId,
        Integer sectorsId,
        Integer areasId,
        Integer areaId,
        Integer projectStatusesId,
        Integer projectStatusId,
        Integer originProjectsId,
        Integer parentProjectId,
        Integer countriesId,
        Integer countryId,
        Integer clientsSuppliersId,
        Integer clientSupplierId,
        Integer lastProjectStatusesId,
        Integer coinTypeId,
        Integer originProposalId,
        Integer proposalId,
        Integer departmentId,
        Integer officeId,
        Integer sectorId,
        Integer responsibleUserId,
        Boolean billableFl,
        Boolean internationalFl,
        String projectDir,
        String site,
        Boolean isDefault,
        BigDecimal exchangeRate,
        BigDecimal exchangeRateFrom,
        BigDecimal exchangeRateTo,
        String openingEmail,
        String classification,
        Boolean investimentFl,
        Boolean productFl,
        LocalDate startDateFrom,
        LocalDate startDateTo,
        LocalDate endDateFrom,
        LocalDate endDateTo,
        BigDecimal budgetFrom,
        BigDecimal budgetTo,
        Boolean active
) {
    
    public Integer getNormalizedDepartmentId() {
        return departmentId != null ? departmentId : departmentsId;
    }
    
    public Integer getNormalizedProjectTypeId() {
        return projectTypeId != null ? projectTypeId : projectTypesId;
    }
    
    public Integer getNormalizedAreaId() {
        return areaId != null ? areaId : areasId;
    }
    
    public Integer getNormalizedProjectStatusId() {
        return projectStatusId != null ? projectStatusId : projectStatusesId;
    }
    
    public Integer getNormalizedParentProjectId() {
        return parentProjectId != null ? parentProjectId : originProjectsId;
    }
    
    public Integer getNormalizedCountryId() {
        return countryId != null ? countryId : countriesId;
    }
    
    public Integer getNormalizedClientSupplierId() {
        return clientSupplierId != null ? clientSupplierId : clientsSuppliersId;
    }
    
    public Integer getNormalizedProposalId() {
        return proposalId != null ? proposalId : originProposalId;
    }
    
    public Integer getNormalizedSectorId() {
        return sectorId != null ? sectorId : sectorsId;
    }
}
