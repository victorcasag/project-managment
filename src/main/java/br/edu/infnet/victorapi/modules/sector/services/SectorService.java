package br.edu.infnet.victorapi.modules.sector.services;

import br.edu.infnet.victorapi.modules.sector.entity.Sector;
import br.edu.infnet.victorapi.modules.sector.repository.ISectorRepository;
import br.edu.infnet.victorapi.modules.sector.repository.SectorRepository;
import br.edu.infnet.victorapi.modules.sector.dto.CreateSectorDTO;
import br.edu.infnet.victorapi.modules.sector.dto.UpdateSectorDTO;
import br.edu.infnet.victorapi.modules.sector.dto.SectorResponseDTO;
import br.edu.infnet.victorapi.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SectorService {

    private static final String ENTITY_NAME = "Setor";

    private final ISectorRepository sectorRepository;
    private final SectorRepository sectorRepositoryImpl;

    @Autowired
    public SectorService(ISectorRepository sectorRepository, SectorRepository sectorRepositoryImpl) {
        this.sectorRepository = sectorRepository;
        this.sectorRepositoryImpl = sectorRepositoryImpl;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Sector createSector(CreateSectorDTO createSectorDTO) {
        if (!isValidSectorName(createSectorDTO.name())) {
            throw new EntityInvalidException(ENTITY_NAME, "name", createSectorDTO.name(),
                    "Nome deve ter entre 1 e 255 caracteres e não pode estar vazio");
        }

        if (createSectorDTO.code() != null && !isValidSectorCode(createSectorDTO.code())) {
            throw new EntityInvalidException(ENTITY_NAME, "code", createSectorDTO.code(),
                    "Código deve ter no máximo 50 caracteres");
        }

        if (sectorRepositoryImpl.existsByName(createSectorDTO.name())) {
            throw new EntityAlreadyExistsException(ENTITY_NAME, "name", createSectorDTO.name());
        }

        if (createSectorDTO.code() != null && !createSectorDTO.code().trim().isEmpty()) {
            if (sectorRepository.existsByCode(createSectorDTO.code())) {
                throw new EntityAlreadyExistsException(ENTITY_NAME, "code", createSectorDTO.code());
            }
        }

        try {
            Sector sector = new Sector();
            sector.setName(createSectorDTO.name());
            sector.setCode(createSectorDTO.code());
            sector.setDescription(createSectorDTO.description());

            return sectorRepository.save(sector);
        } catch (Exception e) {
            throw new BusinessOperationException("createSector", ENTITY_NAME, null,
                    "Erro interno ao salvar: " + e.getMessage());
        }
    }

    public Optional<Sector> getSectorById(Integer sectorId) {
        if (sectorId == null || sectorId <= 0) {
            throw new EntityInvalidException(ENTITY_NAME, "id", String.valueOf(sectorId),
                    "ID deve ser um número positivo");
        }

        return sectorRepository.findById(sectorId);
    }

    public Sector getSectorByIdRequired(Integer sectorId) {
        return getSectorById(sectorId)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, sectorId));
    }

    public Optional<Sector> getSectorByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new EntityInvalidException(ENTITY_NAME, "code", code,
                    "Código não pode estar vazio");
        }

        return sectorRepository.findByCode(code);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public Page<Sector> getAllSectors(Pageable pageable) {
        try {
            return sectorRepository.findByIsActiveTrue(pageable);
        } catch (Exception e) {
            throw new BusinessOperationException("getAllSectors",
                    "Erro ao buscar setores: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public List<Sector> getAllActiveSectors() {
        try {
            return sectorRepository.findByIsActiveTrue();
        } catch (Exception e) {
            throw new BusinessOperationException("getAllActiveSectors",
                    "Erro ao buscar setores ativos: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Sector updateSector(Integer sectorId, UpdateSectorDTO updateSectorDTO) {
        Sector sector = getSectorByIdRequired(sectorId);

        if (!sector.getIsActive()) {
            throw new EntityDeactivatedException(ENTITY_NAME, sectorId);
        }

        if (updateSectorDTO.name() != null) {
            if (!isValidSectorName(updateSectorDTO.name())) {
                throw new EntityInvalidException(ENTITY_NAME, "name", updateSectorDTO.name(),
                        "Nome deve ter entre 1 e 255 caracteres");
            }

            if (!updateSectorDTO.name().equals(sector.getName()) &&
                    sectorRepositoryImpl.existsByName(updateSectorDTO.name())) {
                throw new EntityAlreadyExistsException(ENTITY_NAME, "name", updateSectorDTO.name());
            }
        }

        if (updateSectorDTO.code() != null) {
            if (!isValidSectorCode(updateSectorDTO.code())) {
                throw new EntityInvalidException(ENTITY_NAME, "code", updateSectorDTO.code(),
                        "Código deve ter no máximo 50 caracteres");
            }

            if (!updateSectorDTO.code().equals(sector.getCode()) &&
                    sectorRepository.existsByCode(updateSectorDTO.code())) {
                throw new EntityAlreadyExistsException(ENTITY_NAME, "code", updateSectorDTO.code());
            }
        }

        try {
            if (updateSectorDTO.name() != null) {
                sector.setName(updateSectorDTO.name());
            }
            if (updateSectorDTO.code() != null) {
                sector.setCode(updateSectorDTO.code());
            }
            if (updateSectorDTO.description() != null) {
                sector.setDescription(updateSectorDTO.description());
            }

            return sectorRepository.save(sector);
        } catch (Exception e) {
            throw new BusinessOperationException("updateSector", ENTITY_NAME,
                    String.valueOf(sectorId), "Erro ao atualizar: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteSector(Integer sectorId) {
        Sector sector = getSectorByIdRequired(sectorId);

        if (!sector.getIsActive()) {
            throw new EntityDeactivatedException(ENTITY_NAME, sectorId);
        }

        try {
            sectorRepositoryImpl.deactivateSector(sectorId);
        } catch (Exception e) {
            throw new BusinessOperationException("deleteSector", ENTITY_NAME,
                    String.valueOf(sectorId), "Erro ao desativar: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public List<Sector> searchSectorsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EntityInvalidException(ENTITY_NAME, "name", name,
                    "Nome para busca não pode estar vazio");
        }

        try {
            return sectorRepository.findByNameContainingIgnoreCase(name);
        } catch (Exception e) {
            throw new BusinessOperationException("searchSectorsByName",
                    "Erro ao buscar setores por nome: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public Page<Sector> getSectorsWithFilters(String name, String code, Boolean isActive, Pageable pageable) {
        try {
            return sectorRepositoryImpl.findSectorsWithFilters(name, code, isActive, pageable);
        } catch (Exception e) {
            throw new BusinessOperationException("getSectorsWithFilters",
                    "Erro ao buscar setores com filtros: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean activateSector(Integer sectorId) {
        Sector sector = getSectorByIdRequired(sectorId);

        if (sector.getIsActive()) {
            throw new BusinessOperationException("activateSector", ENTITY_NAME,
                    String.valueOf(sectorId), "Setor já está ativo");
        }

        try {
            return sectorRepositoryImpl.activateSector(sectorId);
        } catch (Exception e) {
            throw new BusinessOperationException("activateSector", ENTITY_NAME,
                    String.valueOf(sectorId), "Erro ao ativar: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean deactivateSector(Integer sectorId) {
        Sector sector = getSectorByIdRequired(sectorId);

        if (!sector.getIsActive()) {
            throw new BusinessOperationException("deactivateSector", ENTITY_NAME,
                    String.valueOf(sectorId), "Setor já está desativado");
        }

        try {
            return sectorRepositoryImpl.deactivateSector(sectorId);
        } catch (Exception e) {
            throw new BusinessOperationException("deactivateSector", ENTITY_NAME,
                    String.valueOf(sectorId), "Erro ao desativar: " + e.getMessage());
        }
    }

    public boolean existsByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return sectorRepository.existsByCode(code);
    }

    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return sectorRepositoryImpl.existsByName(name);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long countActiveSectors() {
        try {
            return sectorRepository.countActiveSectors();
        } catch (Exception e) {
            throw new BusinessOperationException("countActiveSectors",
                    "Erro ao contar setores ativos: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Sector> getRecentSectors(int days) {
        if (days < 0) {
            throw new EntityInvalidException(ENTITY_NAME, "days", String.valueOf(days),
                    "Número de dias deve ser positivo");
        }

        try {
            LocalDateTime startDate = LocalDateTime.now().minusDays(days);
            LocalDateTime endDate = LocalDateTime.now();
            return sectorRepository.findSectorsCreatedBetween(startDate, endDate);
        } catch (Exception e) {
            throw new BusinessOperationException("getRecentSectors",
                    "Erro ao buscar setores recentes: " + e.getMessage());
        }
    }

    public SectorResponseDTO convertToResponseDTO(Sector sector) {
        if (sector == null) {
            throw new EntityInvalidException(ENTITY_NAME, "sector", "null",
                    "Setor não pode ser nulo");
        }

        return new SectorResponseDTO(
                sector.getId(),
                sector.getName(),
                sector.getCode(),
                sector.getDescription(),
                sector.getIsActive(),
                sector.getCreatedAt(),
                sector.getUpdatedAt()
        );
    }

    public List<SectorResponseDTO> convertToResponseDTOList(List<Sector> sectors) {
        if (sectors == null) {
            throw new EntityInvalidException(ENTITY_NAME, "sectors", "null",
                    "Lista de setores não pode ser nula");
        }

        return sectors.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public boolean isValidSectorCode(String code) {
        return code != null && !code.trim().isEmpty() && code.length() <= 50;
    }

    public boolean isValidSectorName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 255;
    }
}