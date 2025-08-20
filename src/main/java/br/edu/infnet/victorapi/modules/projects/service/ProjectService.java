package br.edu.infnet.victorapi.modules.projects.service;

import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.projects.dto.CheckCodeResponseDTO;
import br.edu.infnet.victorapi.modules.projects.dto.CreateProjectDTO;
import br.edu.infnet.victorapi.modules.projects.dto.ProjectFilterDTO;
import br.edu.infnet.victorapi.modules.projects.dto.ProjectResponseDTO;
import br.edu.infnet.victorapi.modules.projects.dto.UpdateProjectDTO;
import br.edu.infnet.victorapi.modules.projects.entity.Project;

import br.edu.infnet.victorapi.modules.projects.repository.IProjectRepository;
import br.edu.infnet.victorapi.modules.projects.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private IProjectRepository projectRepository;

    @Autowired
    private ProjectRepository customProjectRepository;

    @PreAuthorize("hasRole('USER')")
    public List<ProjectResponseDTO> findAll() {
        List<Project> projects = projectRepository.findAllByActiveTrueOrderByName();
        return projects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public Page<ProjectResponseDTO> findAll(Pageable pageable) {
        Page<Project> projects = projectRepository.findAllByActiveTrueOrderByName(pageable);
        return projects.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public Page<ProjectResponseDTO> findWithFilters(ProjectFilterDTO filters, Pageable pageable) {
        Page<Project> projects = customProjectRepository.findWithFilters(
                filters.name(), 
                filters.getNormalizedDepartmentId(), 
                filters.getNormalizedProjectTypeId(),
                filters.getNormalizedSectorId(), 
                filters.getNormalizedAreaId(), 
                filters.getNormalizedProjectStatusId(), 
                filters.getNormalizedParentProjectId(), 
                filters.getNormalizedCountryId(), 
                filters.getNormalizedClientSupplierId(), 
                filters.lastProjectStatusesId(),
                filters.coinTypeId(), 
                filters.getNormalizedProposalId(), 
                filters.billableFl(), 
                filters.internationalFl(), 
                filters.projectDir(), 
                filters.site(), 
                filters.isDefault(), 
                filters.exchangeRateFrom(),
                filters.exchangeRateTo(), 
                filters.openingEmail(), 
                filters.classification(), 
                filters.investimentFl(), 
                filters.productFl(), 
                pageable);
        return projects.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public Page<ProjectResponseDTO> findWithFiltersSimple(ProjectFilterDTO filters, Pageable pageable) {
        List<Project> allProjects = projectRepository.findAllByActiveTrueOrderByName();

        List<Project> filteredProjects = allProjects.stream()
                .filter(project -> filters.name() == null || project.getName().toLowerCase().contains(filters.name().toLowerCase()))
                .filter(project -> filters.getNormalizedDepartmentId() == null || filters.getNormalizedDepartmentId().equals(project.getDepartmentsId()))
                .filter(project -> filters.getNormalizedProjectTypeId() == null || filters.getNormalizedProjectTypeId().equals(project.getProjectTypesId()))
                .filter(project -> filters.getNormalizedAreaId() == null || filters.getNormalizedAreaId().equals(project.getAreasId()))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredProjects.size());
        List<Project> pageContent = filteredProjects.subList(start, end);
        
        Page<Project> projects = new org.springframework.data.domain.PageImpl<>(pageContent, pageable, filteredProjects.size());
        return projects.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public ProjectResponseDTO findById(Integer id) {
        Project project = projectRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("Project", id));

        return toDTO(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProjectResponseDTO create(CreateProjectDTO dto) {
        Project project = new Project();
        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setDepartmentsId(dto.departmentsId());
        project.setProjectTypesId(dto.projectTypesId());
        project.setSectorsId(dto.sectorsId());
        project.setAreasId(dto.areasId());
        project.setProjectStatusesId(dto.projectStatusesId());
        project.setOriginProjectsId(dto.originProjectsId());
        project.setCountriesId(dto.countriesId());
        project.setClientsSuppliersId(dto.clientsSuppliersId());
        project.setLastProjectStatusesId(dto.lastProjectStatusesId());
        project.setCoinTypeId(dto.coinTypeId());
        project.setOriginProposalId(dto.originProposalId());
        project.setBillableFl(dto.billableFl());
        project.setInternationalFl(dto.internationalFl());
        project.setProjectDir(dto.projectDir());
        project.setSite(dto.site());
        project.setIsDefault(dto.isDefault());
        project.setExchangeRate(dto.exchangeRate());
        project.setOpeningEmail(dto.openingEmail());
        project.setClassification(dto.classification());
        project.setInvestimentFl(dto.investimentFl());
        project.setProductFl(dto.productFl());

        project = projectRepository.save(project);
        return toDTO(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProjectResponseDTO update(Integer id, UpdateProjectDTO dto) {
        Project project = projectRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("Project", id));

        project.setName(dto.name());
        project.setDescription(dto.description());
        project.setDepartmentsId(dto.departmentsId());
        project.setProjectTypesId(dto.projectTypesId());
        project.setSectorsId(dto.sectorsId());
        project.setAreasId(dto.areasId());
        project.setProjectStatusesId(dto.projectStatusesId());
        project.setOriginProjectsId(dto.originProjectsId());
        project.setCountriesId(dto.countriesId());
        project.setClientsSuppliersId(dto.clientsSuppliersId());
        project.setLastProjectStatusesId(dto.lastProjectStatusesId());
        project.setCoinTypeId(dto.coinTypeId());
        project.setOriginProposalId(dto.originProposalId());
        project.setBillableFl(dto.billableFl());
        project.setInternationalFl(dto.internationalFl());
        project.setProjectDir(dto.projectDir());
        project.setSite(dto.site());
        project.setIsDefault(dto.isDefault());
        project.setExchangeRate(dto.exchangeRate());
        project.setOpeningEmail(dto.openingEmail());
        project.setClassification(dto.classification());
        project.setInvestimentFl(dto.investimentFl());
        project.setProductFl(dto.productFl());

        project = projectRepository.save(project);
        return toDTO(project);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Integer id) {
        Project project = projectRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("Project", id));

        projectRepository.delete(project);
    }

    @PreAuthorize("hasRole('USER')")
    public CheckCodeResponseDTO checkCodeAvailability(String name) {
        Long count = projectRepository.countByNameAndActiveTrue(name);
        return new CheckCodeResponseDTO(
                name,
                count == 0,
                count == 0 ? "Nome disponível" : "Nome já existe"
        );
    }

    @PreAuthorize("hasRole('USER')")
    public List<ProjectResponseDTO> findByProjectType(Integer projectTypeId) {
        List<Project> projects = projectRepository.findByProjectTypeIdAndActiveTrue(projectTypeId);
        return projects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<ProjectResponseDTO> findByProjectStatus(Integer projectStatusId) {
        List<Project> projects = projectRepository.findByProjectStatusIdAndActiveTrue(projectStatusId);
        return projects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<ProjectResponseDTO> findByResponsibleUser(Integer responsibleUserId) {
        return List.of();
    }

    public List<ProjectResponseDTO> findByParentProject(Integer parentProjectId) {
        List<Project> projects = projectRepository.findByParentProjectIdAndActiveTrue(parentProjectId);
        return projects.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalBudget() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotalSpentAmount() {
        return BigDecimal.ZERO;
    }

    private ProjectResponseDTO toDTO(Project project) {
        return new ProjectResponseDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getDepartmentsId(),
                project.getProjectTypesId(),
                project.getSectorsId(),
                project.getAreasId(),
                project.getProjectStatusesId(),
                project.getOriginProjectsId(),
                project.getCountriesId(),
                project.getClientsSuppliersId(),
                project.getLastProjectStatusesId(),
                project.getCoinTypeId(),
                project.getOriginProposalId(),
                project.getBillableFl(),
                project.getInternationalFl(),
                project.getProjectDir(),
                project.getSite(),
                project.getIsDefault(),
                project.getExchangeRate(),
                project.getOpeningEmail(),
                project.getClassification(),
                project.getInvestimentFl(),
                project.getProductFl(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
