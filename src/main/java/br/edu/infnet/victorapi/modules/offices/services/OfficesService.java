package br.edu.infnet.victorapi.modules.offices.services;

import br.edu.infnet.victorapi.exceptions.BusinessOperationException;
import br.edu.infnet.victorapi.exceptions.EntityAlreadyExistsException;
import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.offices.dto.CreateOfficesDTO;
import br.edu.infnet.victorapi.modules.offices.dto.OfficesFilterDTO;
import br.edu.infnet.victorapi.modules.offices.dto.OfficesResponseDTO;
import br.edu.infnet.victorapi.modules.offices.dto.UpdateOfficesDTO;
import br.edu.infnet.victorapi.modules.offices.entity.Offices;
import br.edu.infnet.victorapi.modules.offices.repository.IOfficesRepository;
import br.edu.infnet.victorapi.modules.offices.repository.OfficesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OfficesService {

    @Autowired
    private IOfficesRepository officesRepository;

    @Autowired
    private OfficesRepository officesCriteriaRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<OfficesResponseDTO> findAll(Pageable pageable) {
        Page<Offices> offices = officesRepository.findAllActive(pageable);
        return offices.map(this::convertToResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<OfficesResponseDTO> findWithFilters(OfficesFilterDTO filters, Pageable pageable) {
        Page<Offices> offices = officesCriteriaRepository.findWithFilters(
                filters.name(), filters.code(), filters.city(), filters.state(), 
                filters.countryId(), filters.email(), filters.phone(), 
                filters.isMainOffice(), filters.isActive(), pageable);
        return offices.map(this::convertToResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public OfficesResponseDTO findById(Integer id) {
        Offices office = officesRepository.findByIdAndActive(id)
                .orElseThrow(() -> new EntityNotFoundException("Offices", id));
        return convertToResponseDTO(office);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<OfficesResponseDTO> findActive() {
        List<Offices> offices = officesRepository.findAllActive();
        return offices.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public OfficesResponseDTO findByCode(String code) {
        Offices office = officesRepository.findByCodeAndActive(code)
                .orElseThrow(() -> new EntityNotFoundException("Offices", "código", code));
        return convertToResponseDTO(office);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public OfficesResponseDTO findByEmail(String email) {
        Offices office = officesRepository.findByEmailAndActive(email)
                .orElseThrow(() -> new EntityNotFoundException("Offices", "email", email));
        return convertToResponseDTO(office);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public OfficesResponseDTO findMainOffice() {
        Offices office = officesRepository.findMainOffice()
                .orElseThrow(() -> new EntityNotFoundException("Offices", "Escritório principal não encontrado"));
        return convertToResponseDTO(office);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<OfficesResponseDTO> findByCountry(Integer countryId) {
        List<Offices> offices = officesRepository.findByCountry(countryId);
        return offices.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<OfficesResponseDTO> findByCity(String city) {
        List<Offices> offices = officesRepository.findByCity(city);
        return offices.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<OfficesResponseDTO> findByNameContaining(String name) {
        List<Offices> offices = officesRepository.findByNameContainingIgnoreCaseAndActive(name);
        return offices.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<OfficesResponseDTO> searchByName(String name) {
        List<Offices> offices = officesCriteriaRepository.findActiveByNameContaining(name);
        return offices.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<OfficesResponseDTO> searchByCode(String code) {
        List<Offices> offices = officesCriteriaRepository.findActiveByCodeContaining(code);
        return offices.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OfficesResponseDTO create(CreateOfficesDTO dto) {
        // Validate code uniqueness if provided
        if (dto.code() != null && officesRepository.existsByCodeAndIsActiveTrue(dto.code())) {
            throw new EntityAlreadyExistsException("Offices", "código", dto.code());
        }

        // Validate email uniqueness if provided
        if (dto.email() != null && officesRepository.existsByEmailAndIsActiveTrue(dto.email())) {
            throw new EntityAlreadyExistsException("Offices", "email", dto.email());
        }

        // Validate main office logic
        if (dto.isMainOffice() != null && dto.isMainOffice()) {
            Optional<Offices> existingMainOffice = officesRepository.findMainOffice();
            if (existingMainOffice.isPresent()) {
                throw new BusinessOperationException("Já existe um escritório principal. " +
                        "Desative o atual antes de definir outro como principal.");
            }
        }

        Offices office = new Offices(
                dto.name(),
                dto.code(),
                dto.address(),
                dto.city(),
                dto.state(),
                dto.postalCode(),
                dto.countryId(),
                dto.phone(),
                dto.email(),
                dto.isMainOffice()
        );

        Offices savedOffice = officesRepository.save(office);
        return convertToResponseDTO(savedOffice);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OfficesResponseDTO update(Integer id, UpdateOfficesDTO dto) {
        Offices existingOffice = officesRepository.findByIdAndActive(id)
                .orElseThrow(() -> new EntityNotFoundException("Offices", id));

        // Validate code uniqueness (excluding current record)
        if (dto.code() != null && 
            officesRepository.existsByCodeAndIdNotAndIsActiveTrue(dto.code(), id)) {
            throw new EntityAlreadyExistsException("Offices", "código", dto.code());
        }

        // Validate email uniqueness (excluding current record)
        if (dto.email() != null && 
            officesRepository.existsByEmailAndIdNotAndIsActiveTrue(dto.email(), id)) {
            throw new EntityAlreadyExistsException("Offices", "email", dto.email());
        }

        // Validate main office logic
        if (dto.isMainOffice() != null && dto.isMainOffice()) {
            if (officesRepository.existsByIsMainOfficeAndIsActiveTrueAndIdNot(true, id)) {
                throw new BusinessOperationException("Já existe outro escritório principal. " +
                        "Desative o atual antes de definir este como principal.");
            }
        }

        // Update fields
        if (dto.name() != null) {
            existingOffice.setName(dto.name());
        }
        if (dto.code() != null) {
            existingOffice.setCode(dto.code());
        }
        if (dto.address() != null) {
            existingOffice.setAddress(dto.address());
        }
        if (dto.city() != null) {
            existingOffice.setCity(dto.city());
        }
        if (dto.state() != null) {
            existingOffice.setState(dto.state());
        }
        if (dto.postalCode() != null) {
            existingOffice.setPostalCode(dto.postalCode());
        }
        if (dto.countryId() != null) {
            existingOffice.setCountryId(dto.countryId());
        }
        if (dto.phone() != null) {
            existingOffice.setPhone(dto.phone());
        }
        if (dto.email() != null) {
            existingOffice.setEmail(dto.email());
        }
        if (dto.isMainOffice() != null) {
            existingOffice.setIsMainOffice(dto.isMainOffice());
        }
        if (dto.isActive() != null) {
            existingOffice.setIsActive(dto.isActive());
        }

        Offices updatedOffice = officesRepository.save(existingOffice);
        return convertToResponseDTO(updatedOffice);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Integer id) {
        Offices office = officesRepository.findByIdAndActive(id)
                .orElseThrow(() -> new EntityNotFoundException("Offices", id));

        // Prevent deletion of main office without confirmation
        if (office.getIsMainOffice() != null && office.getIsMainOffice()) {
            throw new BusinessOperationException("Não é possível desativar o escritório principal. " +
                    "Defina outro escritório como principal primeiro.");
        }

        office.setIsActive(false);
        officesRepository.save(office);
    }

    private OfficesResponseDTO convertToResponseDTO(Offices office) {
        return new OfficesResponseDTO(
                office.getId(),
                office.getName(),
                office.getCode(),
                office.getAddress(),
                office.getCity(),
                office.getState(),
                office.getPostalCode(),
                office.getCountryId(),
                office.getPhone(),
                office.getEmail(),
                office.getIsMainOffice(),
                office.getIsActive(),
                office.getCreatedAt(),
                office.getUpdatedAt()
        );
    }
}
