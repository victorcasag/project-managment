package br.edu.infnet.victorapi.modules.projectstatus.service;

import br.edu.infnet.victorapi.exceptions.EntityAlreadyExistsException;
import br.edu.infnet.victorapi.exceptions.EntityDeactivatedException;
import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.projectstatus.dto.CheckCodeResponseDTO;
import br.edu.infnet.victorapi.modules.projectstatus.dto.CreateProjectStatusDTO;
import br.edu.infnet.victorapi.modules.projectstatus.dto.ProjectStatusResponseDTO;
import br.edu.infnet.victorapi.modules.projectstatus.dto.UpdateProjectStatusDTO;
import br.edu.infnet.victorapi.modules.projectstatus.entity.ProjectStatus;
import br.edu.infnet.victorapi.modules.projectstatus.repository.IProjectStatusRepository;
import br.edu.infnet.victorapi.modules.projectstatus.repository.ProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectStatusService {

    @Autowired
    private IProjectStatusRepository projectStatusRepository;

    @Autowired
    private ProjectStatusRepository customProjectStatusRepository;

    @PreAuthorize("hasRole('USER')")
    public List<ProjectStatusResponseDTO> findAll() {
        List<ProjectStatus> projectStatuses = projectStatusRepository.findAllByActiveTrueOrderBySortOrderAndName();
        return projectStatuses.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public Page<ProjectStatusResponseDTO> findAll(Pageable pageable) {
        Page<ProjectStatus> projectStatuses = projectStatusRepository.findAllByActiveTrueOrderBySortOrderAndName(pageable);
        return projectStatuses.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public Page<ProjectStatusResponseDTO> findWithFilters(String name, String code, String color, Boolean isActive, Boolean isInitial, Boolean isFinal, Pageable pageable) {
        Page<ProjectStatus> projectStatuses = customProjectStatusRepository.findWithFilters(name, code, color, isActive, isInitial, isFinal, pageable);
        return projectStatuses.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public ProjectStatusResponseDTO findById(Integer id) {
        ProjectStatus projectStatus = projectStatusRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("ProjectStatus", id));

        if (!projectStatus.getIsActive()) {
            throw new EntityDeactivatedException("ProjectStatus", "Status do projeto está desativado");
        }

        return toDTO(projectStatus);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProjectStatusResponseDTO create(CreateProjectStatusDTO dto) {
        if (projectStatusRepository.countByCodeAndActiveTrue(dto.code()) > 0) {
            throw new EntityAlreadyExistsException("ProjectStatus", "Status do projeto com este código já existe");
        }

        if (projectStatusRepository.countByNameAndActiveTrue(dto.name()) > 0) {
            throw new EntityAlreadyExistsException("ProjectStatus", "Status do projeto com este nome já existe");
        }

        ProjectStatus projectStatus = new ProjectStatus();
        projectStatus.setName(dto.name());
        projectStatus.setCode(dto.code());
        projectStatus.setDescription(dto.description());
        projectStatus.setColor(dto.color());
        projectStatus.setSortOrder(dto.sortOrder() != null ? dto.sortOrder() : 0);
        projectStatus.setIsActive(true);
        projectStatus.setIsInitial(dto.isInitial() != null ? dto.isInitial() : false);
        projectStatus.setIsFinal(dto.isFinal() != null ? dto.isFinal() : false);
        projectStatus.setCreatedAt(LocalDateTime.now());
        projectStatus.setUpdatedAt(LocalDateTime.now());

        projectStatus = projectStatusRepository.save(projectStatus);
        return toDTO(projectStatus);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProjectStatusResponseDTO update(Integer id, UpdateProjectStatusDTO dto) {
        ProjectStatus projectStatus = projectStatusRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("ProjectStatus", id));

        if (!projectStatus.getIsActive()) {
            throw new EntityDeactivatedException("ProjectStatus", "Status do projeto está desativado");
        }

        if (projectStatusRepository.countByCodeAndIdNotAndActiveTrue(dto.code(), Long.valueOf(id)) > 0) {
            throw new EntityAlreadyExistsException("ProjectStatus", "Status do projeto com este código já existe");
        }

        if (projectStatusRepository.countByNameAndIdNotAndActiveTrue(dto.name(), Long.valueOf(id)) > 0) {
            throw new EntityAlreadyExistsException("ProjectStatus", "Status do projeto com este nome já existe");
        }

        projectStatus.setName(dto.name());
        projectStatus.setCode(dto.code());
        projectStatus.setDescription(dto.description());
        projectStatus.setColor(dto.color());
        projectStatus.setSortOrder(dto.sortOrder() != null ? dto.sortOrder() : 0);
        projectStatus.setIsInitial(dto.isInitial() != null ? dto.isInitial() : false);
        projectStatus.setIsFinal(dto.isFinal() != null ? dto.isFinal() : false);
        projectStatus.setUpdatedAt(LocalDateTime.now());

        projectStatus = projectStatusRepository.save(projectStatus);
        return toDTO(projectStatus);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Integer id) {
        ProjectStatus projectStatus = projectStatusRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("ProjectStatus", id));

        projectStatus.setIsActive(false);
        projectStatus.setUpdatedAt(LocalDateTime.now());
        projectStatusRepository.save(projectStatus);
    }

    @PreAuthorize("hasRole('USER')")
    public CheckCodeResponseDTO checkCodeAvailability(String code) {
        Long count = projectStatusRepository.countByCodeAndActiveTrue(code);
        return new CheckCodeResponseDTO(
                code,
                count == 0,
                count == 0 ? "Código disponível" : "Código já existe"
        );
    }

    @PreAuthorize("hasRole('USER')")
    public ProjectStatusResponseDTO findInitialStatus() {
        ProjectStatus projectStatus = projectStatusRepository.findInitialStatus()
                .orElseThrow(() -> new EntityNotFoundException("ProjectStatus", "Status inicial não encontrado"));

        return toDTO(projectStatus);
    }

    @PreAuthorize("hasRole('USER')")
    public List<ProjectStatusResponseDTO> findFinalStatuses() {
        List<ProjectStatus> projectStatuses = projectStatusRepository.findFinalStatuses();
        return projectStatuses.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProjectStatusResponseDTO toDTO(ProjectStatus projectStatus) {
        return new ProjectStatusResponseDTO(
                projectStatus.getId(),
                projectStatus.getName(),
                projectStatus.getCode(),
                projectStatus.getDescription(),
                projectStatus.getColor(),
                projectStatus.getSortOrder(),
                projectStatus.getIsActive(),
                projectStatus.getIsInitial(),
                projectStatus.getIsFinal(),
                projectStatus.getCreatedAt(),
                projectStatus.getUpdatedAt()
        );
    }
}
