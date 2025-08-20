package br.edu.infnet.victorapi.modules.departments.services;

import br.edu.infnet.victorapi.exceptions.EntityAlreadyExistsException;
import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.departments.dto.DepartmentFilterDTO;
import br.edu.infnet.victorapi.modules.departments.dto.DepartmentResponseDTO;
import br.edu.infnet.victorapi.modules.departments.dto.CreateDepartmentDTO;
import br.edu.infnet.victorapi.modules.departments.dto.UpdateDepartmentDTO;
import br.edu.infnet.victorapi.modules.departments.entity.Departments;
import br.edu.infnet.victorapi.modules.departments.repository.DepartmentRepository;
import br.edu.infnet.victorapi.modules.departments.repository.IDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private IDepartmentRepository departmentRepository;

    @Autowired
    private DepartmentRepository customDepartmentRepository;

    @PreAuthorize("hasRole('USER')")
    public List<DepartmentResponseDTO> findAll() {
        List<Departments> departments = departmentRepository.findAllOrderByName();
        return departments.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public Page<DepartmentResponseDTO> findAll(Pageable pageable) {
        Page<Departments> departments = departmentRepository.findAllOrderByName(pageable);
        return departments.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public Page<DepartmentResponseDTO> findWithFilters(DepartmentFilterDTO filters, Pageable pageable) {
        Page<Departments> departments = customDepartmentRepository.findDepartmentsWithFilters(
                filters.name(), filters.code(), filters.description(), filters.isActive(), pageable);
        return departments.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public DepartmentResponseDTO findById(Integer id) {
        Departments department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department", id));
        return toDTO(department);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DepartmentResponseDTO create(CreateDepartmentDTO dto) {
        if (dto.code() != null && departmentRepository.existsByCode(dto.code())) {
            throw new EntityAlreadyExistsException("Department", "Departamento com este código já existe");
        }

        if (departmentRepository.countByName(dto.name()) > 0) {
            throw new EntityAlreadyExistsException("Department", "Departamento com este nome já existe");
        }

        Departments department = new Departments();
        department.setName(dto.name());
        department.setCode(dto.code());
        department.setDescription(dto.description());
        department.setIsActive(true);

        department = departmentRepository.save(department);
        return toDTO(department);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public DepartmentResponseDTO update(Integer id, UpdateDepartmentDTO dto) {
        Departments department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department", id));

        if (dto.name() != null) {
            department.setName(dto.name());
        }
        if (dto.code() != null) {
            department.setCode(dto.code());
        }
        if (dto.description() != null) {
            department.setDescription(dto.description());
        }
        if (dto.isActive() != null) {
            department.setIsActive(dto.isActive());
        }

        department = departmentRepository.save(department);
        return toDTO(department);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Integer id) {
        Departments department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department", id));

        department.setIsActive(false);
        departmentRepository.save(department);
    }

    @PreAuthorize("hasRole('USER')")
    public List<DepartmentResponseDTO> findActive() {
        List<Departments> departments = departmentRepository.findAllActive();
        return departments.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public DepartmentResponseDTO findByCode(String code) {
        Departments department = departmentRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Department", "Código: " + code));
        return toDTO(department);
    }

    private DepartmentResponseDTO toDTO(Departments department) {
        return new DepartmentResponseDTO(
                department.getId(),
                department.getName(),
                department.getCode(),
                department.getDescription(),
                department.getIsActive(),
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }
}
