package br.edu.infnet.victorapi.modules.clientsupplier.services;

import br.edu.infnet.victorapi.exceptions.EntityAlreadyExistsException;
import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.clientsupplier.dto.ClientSupplierFilterDTO;
import br.edu.infnet.victorapi.modules.clientsupplier.dto.ClientSupplierResponseDTO;
import br.edu.infnet.victorapi.modules.clientsupplier.dto.CreateClientSupplierDTO;
import br.edu.infnet.victorapi.modules.clientsupplier.dto.UpdateClientSupplierDTO;
import br.edu.infnet.victorapi.modules.clientsupplier.entity.ClientSupplier;
import br.edu.infnet.victorapi.modules.clientsupplier.repository.ClientSupplierRepository;
import br.edu.infnet.victorapi.modules.clientsupplier.repository.IClientSupplierRepository;
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
public class ClientSupplierService {

    @Autowired
    private IClientSupplierRepository clientSupplierRepository;

    @Autowired
    private ClientSupplierRepository clientSupplierCriteriaRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<ClientSupplierResponseDTO> findAll(Pageable pageable) {
        Page<ClientSupplier> clientSuppliers = clientSupplierRepository.findAllActive(pageable);
        return clientSuppliers.map(this::convertToResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Page<ClientSupplierResponseDTO> findWithFilters(ClientSupplierFilterDTO filters, Pageable pageable) {
        Page<ClientSupplier> clientSuppliers = clientSupplierCriteriaRepository.findWithFilters(
                filters.name(), filters.document(), filters.documentType(), filters.email(), 
                filters.phone(), filters.city(), filters.state(), filters.type(), 
                filters.countryId(), filters.isActive(), pageable);
        return clientSuppliers.map(this::convertToResponseDTO);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ClientSupplierResponseDTO findById(Integer id) {
        ClientSupplier clientSupplier = clientSupplierRepository.findByIdAndActive(id)
                .orElseThrow(() -> new EntityNotFoundException("ClientSupplier", id));
        return convertToResponseDTO(clientSupplier);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<ClientSupplierResponseDTO> findActive() {
        List<ClientSupplier> clientSuppliers = clientSupplierRepository.findAllActive();
        return clientSuppliers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ClientSupplierResponseDTO findByDocument(String document) {
        ClientSupplier clientSupplier = clientSupplierRepository.findByDocumentAndActive(document)
                .orElseThrow(() -> new EntityNotFoundException("ClientSupplier", "documento", document));
        return convertToResponseDTO(clientSupplier);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ClientSupplierResponseDTO findByEmail(String email) {
        ClientSupplier clientSupplier = clientSupplierRepository.findByEmailAndActive(email)
                .orElseThrow(() -> new EntityNotFoundException("ClientSupplier", "email", email));
        return convertToResponseDTO(clientSupplier);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<ClientSupplierResponseDTO> findByType(String type) {
        List<ClientSupplier> clientSuppliers = clientSupplierRepository.findByType(type);
        return clientSuppliers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<ClientSupplierResponseDTO> findByCountry(Integer countryId) {
        List<ClientSupplier> clientSuppliers = clientSupplierRepository.findByCountry(countryId);
        return clientSuppliers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<ClientSupplierResponseDTO> findByCity(String city) {
        List<ClientSupplier> clientSuppliers = clientSupplierRepository.findByCity(city);
        return clientSuppliers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<ClientSupplierResponseDTO> searchByName(String name) {
        List<ClientSupplier> clientSuppliers = clientSupplierCriteriaRepository.findActiveByNameContaining(name);
        return clientSuppliers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<ClientSupplierResponseDTO> findClients() {
        List<ClientSupplier> clients = clientSupplierCriteriaRepository.findByTypeAndActive("CLIENT");
        return clients.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public List<ClientSupplierResponseDTO> findSuppliers() {
        List<ClientSupplier> suppliers = clientSupplierCriteriaRepository.findByTypeAndActive("SUPPLIER");
        return suppliers.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ClientSupplierResponseDTO create(CreateClientSupplierDTO dto) {
        // Validate document uniqueness
        if (dto.document() != null && clientSupplierRepository.existsByDocumentAndIsActiveTrue(dto.document())) {
            throw new EntityAlreadyExistsException("ClientSupplier", "documento", dto.document());
        }

        // Validate email uniqueness
        if (dto.email() != null && clientSupplierRepository.existsByEmailAndIsActiveTrue(dto.email())) {
            throw new EntityAlreadyExistsException("ClientSupplier", "email", dto.email());
        }

        ClientSupplier clientSupplier = new ClientSupplier(
                dto.name(),
                dto.document(),
                dto.documentType(),
                dto.email(),
                dto.phone(),
                dto.address(),
                dto.city(),
                dto.state(),
                dto.postalCode(),
                dto.countryId(),
                dto.type()
        );

        ClientSupplier savedClientSupplier = clientSupplierRepository.save(clientSupplier);
        return convertToResponseDTO(savedClientSupplier);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ClientSupplierResponseDTO update(Integer id, UpdateClientSupplierDTO dto) {
        ClientSupplier existingClientSupplier = clientSupplierRepository.findByIdAndActive(id)
                .orElseThrow(() -> new EntityNotFoundException("ClientSupplier", id));

        // Validate document uniqueness (excluding current record)
        if (dto.document() != null && 
            clientSupplierRepository.existsByDocumentAndIdNotAndIsActiveTrue(dto.document(), id)) {
            throw new EntityAlreadyExistsException("ClientSupplier", "documento", dto.document());
        }

        // Validate email uniqueness (excluding current record)
        if (dto.email() != null && 
            clientSupplierRepository.existsByEmailAndIdNotAndIsActiveTrue(dto.email(), id)) {
            throw new EntityAlreadyExistsException("ClientSupplier", "email", dto.email());
        }

        // Update fields
        if (dto.name() != null) {
            existingClientSupplier.setName(dto.name());
        }
        if (dto.document() != null) {
            existingClientSupplier.setDocument(dto.document());
        }
        if (dto.documentType() != null) {
            existingClientSupplier.setDocumentType(dto.documentType());
        }
        if (dto.email() != null) {
            existingClientSupplier.setEmail(dto.email());
        }
        if (dto.phone() != null) {
            existingClientSupplier.setPhone(dto.phone());
        }
        if (dto.address() != null) {
            existingClientSupplier.setAddress(dto.address());
        }
        if (dto.city() != null) {
            existingClientSupplier.setCity(dto.city());
        }
        if (dto.state() != null) {
            existingClientSupplier.setState(dto.state());
        }
        if (dto.postalCode() != null) {
            existingClientSupplier.setPostalCode(dto.postalCode());
        }
        if (dto.countryId() != null) {
            existingClientSupplier.setCountryId(dto.countryId());
        }
        if (dto.type() != null) {
            existingClientSupplier.setType(dto.type());
        }
        if (dto.isActive() != null) {
            existingClientSupplier.setIsActive(dto.isActive());
        }

        ClientSupplier updatedClientSupplier = clientSupplierRepository.save(existingClientSupplier);
        return convertToResponseDTO(updatedClientSupplier);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Integer id) {
        ClientSupplier clientSupplier = clientSupplierRepository.findByIdAndActive(id)
                .orElseThrow(() -> new EntityNotFoundException("ClientSupplier", id));

        clientSupplier.setIsActive(false);
        clientSupplierRepository.save(clientSupplier);
    }

    private ClientSupplierResponseDTO convertToResponseDTO(ClientSupplier clientSupplier) {
        return new ClientSupplierResponseDTO(
                clientSupplier.getId(),
                clientSupplier.getName(),
                clientSupplier.getDocument(),
                clientSupplier.getDocumentType(),
                clientSupplier.getEmail(),
                clientSupplier.getPhone(),
                clientSupplier.getAddress(),
                clientSupplier.getCity(),
                clientSupplier.getState(),
                clientSupplier.getPostalCode(),
                clientSupplier.getCountryId(),
                clientSupplier.getType(),
                clientSupplier.getIsActive(),
                clientSupplier.getCreatedAt(),
                clientSupplier.getUpdatedAt()
        );
    }
}
