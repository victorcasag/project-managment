package br.edu.infnet.victorapi.modules.area.services;

import br.edu.infnet.victorapi.exceptions.EntityAlreadyExistsException;
import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.area.dto.AreaFilterDTO;
import br.edu.infnet.victorapi.modules.area.dto.AreaResponseDTO;
import br.edu.infnet.victorapi.modules.area.dto.CreateAreaDTO;
import br.edu.infnet.victorapi.modules.area.dto.UpdateAreaDTO;
import br.edu.infnet.victorapi.modules.area.entity.Area;
import br.edu.infnet.victorapi.modules.area.repository.AreaRepository;
import br.edu.infnet.victorapi.modules.area.repository.IAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AreaService {

    @Autowired
    private IAreaRepository areaRepository;

    @Autowired
    private AreaRepository areaCriteriaRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<AreaResponseDTO> findAll(Pageable pageable) {
        Page<Area> areas = areaRepository.findAllActive(pageable);
        return areas.map(this::convertToResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<AreaResponseDTO> findWithFilters(AreaFilterDTO filterDTO, Pageable pageable) {
        Page<Area> areas = areaCriteriaRepository.findWithFilters(filterDTO.name(), filterDTO.code(), filterDTO.description(), filterDTO.isActive(), pageable);
        return areas.map(this::convertToResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public AreaResponseDTO findById(Integer id) {
        Area area = areaRepository.findByIdAndActive(id)
                .orElseThrow(() -> new EntityNotFoundException("Area", id));
        return convertToResponseDTO(area);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<AreaResponseDTO> findActive() {
        List<Area> areas = areaRepository.findAllActive();
        return areas.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public AreaResponseDTO findByCode(String code) {
        Area area = areaRepository.findByCodeAndActive(code)
                .orElseThrow(() -> new EntityNotFoundException("Area", "código", code));
        return convertToResponseDTO(area);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<AreaResponseDTO> findByNameContaining(String name) {
        List<Area> areas = areaRepository.findByNameContainingIgnoreCaseAndActive(name);
        return areas.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<AreaResponseDTO> searchByName(String name) {
        List<Area> areas = areaCriteriaRepository.findActiveByNameContaining(name);
        return areas.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<AreaResponseDTO> searchByCode(String code) {
        List<Area> areas = areaCriteriaRepository.findActiveByCodeContaining(code);
        return areas.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AreaResponseDTO create(CreateAreaDTO dto) {
        // Validate code uniqueness if provided
        if (dto.code() != null && areaRepository.existsByCodeAndIsActiveTrue(dto.code())) {
            throw new EntityAlreadyExistsException("Area", "código", dto.code());
        }

        Area area = new Area(dto.name(), dto.code(), dto.description());
        Area savedArea = areaRepository.save(area);
        return convertToResponseDTO(savedArea);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AreaResponseDTO update(Integer id, UpdateAreaDTO dto) {
        Area existingArea = areaRepository.findByIdAndActive(id)
                .orElseThrow(() -> new EntityNotFoundException("Area", id));

        // Validate code uniqueness (excluding current record)
        if (dto.code() != null && 
            areaRepository.existsByCodeAndIdNotAndIsActiveTrue(dto.code(), id)) {
            throw new EntityAlreadyExistsException("Area", "código", dto.code());
        }

        // Update fields
        if (dto.name() != null) {
            existingArea.setName(dto.name());
        }
        if (dto.code() != null) {
            existingArea.setCode(dto.code());
        }
        if (dto.description() != null) {
            existingArea.setDescription(dto.description());
        }
        if (dto.isActive() != null) {
            existingArea.setIsActive(dto.isActive());
        }

        Area updatedArea = areaRepository.save(existingArea);
        return convertToResponseDTO(updatedArea);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Integer id) {
        Area area = areaRepository.findByIdAndActive(id)
                .orElseThrow(() -> new EntityNotFoundException("Area", id));

        area.setIsActive(false);
        areaRepository.save(area);
    }

    private AreaResponseDTO convertToResponseDTO(Area area) {
        return new AreaResponseDTO(
                area.getId(),
                area.getName(),
                area.getCode(),
                area.getDescription(),
                area.getIsActive(),
                area.getCreatedAt(),
                area.getUpdatedAt()
        );
    }
}
