package br.edu.infnet.victorapi.modules.projecttype.service;

import br.edu.infnet.victorapi.exceptions.EntityAlreadyExistsException;
import br.edu.infnet.victorapi.exceptions.EntityDeactivatedException;
import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.projecttype.dto.CheckCodeResponseDTO;
import br.edu.infnet.victorapi.modules.projecttype.dto.CreateProjectTypeDTO;
import br.edu.infnet.victorapi.modules.projecttype.dto.ProjectTypeResponseDTO;
import br.edu.infnet.victorapi.modules.projecttype.dto.UpdateProjectTypeDTO;
import br.edu.infnet.victorapi.modules.projecttype.entity.ProjectType;
import br.edu.infnet.victorapi.modules.projecttype.repository.IProjectTypeRepository;
import br.edu.infnet.victorapi.modules.projecttype.repository.ProjectTypeRepository;
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
public class ProjectTypeService {

    @Autowired
    private IProjectTypeRepository projectTypeRepository;

    @Autowired
    private ProjectTypeRepository customProjectTypeRepository;

    @PreAuthorize("hasRole('USER')")
    public List<ProjectTypeResponseDTO> findAll() {
        List<ProjectType> projectTypes = projectTypeRepository.findAllByActiveTrueOrderByName();
        return projectTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public Page<ProjectTypeResponseDTO> findAll(Pageable pageable) {
        Page<ProjectType> projectTypes = projectTypeRepository.findAllByActiveTrueOrderByName(pageable);
        return projectTypes.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public Page<ProjectTypeResponseDTO> findWithFilters(String name, String code, Boolean active, Pageable pageable) {
        Page<ProjectType> projectTypes = customProjectTypeRepository.findWithFilters(name, code, active, pageable);
        return projectTypes.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public ProjectTypeResponseDTO findById(Integer id) {
        ProjectType projectType = projectTypeRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("ProjectType", id));

        if (!projectType.getIsActive()) {
            throw new EntityDeactivatedException("ProjectType", "Tipo de projeto está desativado");
        }

        return toDTO(projectType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProjectTypeResponseDTO create(CreateProjectTypeDTO dto) {
        if (projectTypeRepository.countByCodeAndActiveTrue(dto.code()) > 0) {
            throw new EntityAlreadyExistsException("ProjectType", "Tipo de projeto com este código já existe");
        }

        if (projectTypeRepository.countByNameAndActiveTrue(dto.name()) > 0) {
            throw new EntityAlreadyExistsException("ProjectType", "Tipo de projeto com este nome já existe");
        }

        ProjectType projectType = new ProjectType();
        projectType.setName(dto.name());
        projectType.setCode(dto.code());
        projectType.setDescription(dto.description());
        projectType.setIsActive(true);
        projectType.setCreatedAt(LocalDateTime.now());
        projectType.setUpdatedAt(LocalDateTime.now());

        projectType = projectTypeRepository.save(projectType);
        return toDTO(projectType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProjectTypeResponseDTO update(Integer id, UpdateProjectTypeDTO dto) {
        ProjectType projectType = projectTypeRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("ProjectType", id));

        if (!projectType.getIsActive()) {
            throw new EntityDeactivatedException("ProjectType", "Tipo de projeto está desativado");
        }

        if (projectTypeRepository.countByCodeAndIdNotAndActiveTrue(dto.code(), Long.valueOf(id)) > 0) {
            throw new EntityAlreadyExistsException("ProjectType", "Tipo de projeto com este código já existe");
        }

        if (projectTypeRepository.countByNameAndIdNotAndActiveTrue(dto.name(), Long.valueOf(id)) > 0) {
            throw new EntityAlreadyExistsException("ProjectType", "Tipo de projeto com este nome já existe");
        }

        projectType.setName(dto.name());
        projectType.setCode(dto.code());
        projectType.setDescription(dto.description());
        projectType.setUpdatedAt(LocalDateTime.now());

        projectType = projectTypeRepository.save(projectType);
        return toDTO(projectType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Integer id) {
        ProjectType projectType = projectTypeRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("ProjectType", id));

        projectType.setIsActive(false);
        projectType.setUpdatedAt(LocalDateTime.now());
        projectTypeRepository.save(projectType);
    }

    @PreAuthorize("hasRole('USER')")
    public CheckCodeResponseDTO checkCodeAvailability(String code) {
        Long count = projectTypeRepository.countByCodeAndActiveTrue(code);
        return new CheckCodeResponseDTO(
                code,
                count == 0,
                count == 0 ? "Código disponível" : "Código já existe"
        );
    }

    private ProjectTypeResponseDTO toDTO(ProjectType projectType) {
        return new ProjectTypeResponseDTO(
                projectType.getId(),
                projectType.getName(),
                projectType.getCode(),
                projectType.getDescription(),
                projectType.getIsActive(),
                projectType.getCreatedAt(),
                projectType.getUpdatedAt()
        );
    }
}
