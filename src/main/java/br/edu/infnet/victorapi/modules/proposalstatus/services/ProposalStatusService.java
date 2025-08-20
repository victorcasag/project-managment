package br.edu.infnet.victorapi.modules.proposalstatus.services;

import br.edu.infnet.victorapi.modules.proposalstatus.entity.ProposalStatus;
import br.edu.infnet.victorapi.modules.proposalstatus.repository.IProposalStatusRepository;
import br.edu.infnet.victorapi.modules.proposalstatus.repository.ProposalStatusRepository;
import br.edu.infnet.victorapi.modules.proposalstatus.dto.CreateProposalStatusDTO;
import br.edu.infnet.victorapi.modules.proposalstatus.dto.UpdateProposalStatusDTO;
import br.edu.infnet.victorapi.modules.proposalstatus.dto.ProposalStatusResponseDTO;
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
public class ProposalStatusService {

    private static final String ENTITY_NAME = "Status de Proposta";

    private final IProposalStatusRepository proposalStatusRepository;
    private final ProposalStatusRepository proposalStatusRepositoryImpl;

    @Autowired
    public ProposalStatusService(IProposalStatusRepository proposalStatusRepository, 
                               ProposalStatusRepository proposalStatusRepositoryImpl) {
        this.proposalStatusRepository = proposalStatusRepository;
        this.proposalStatusRepositoryImpl = proposalStatusRepositoryImpl;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProposalStatus createProposalStatus(CreateProposalStatusDTO createProposalStatusDTO) {
        if (!isValidProposalStatusName(createProposalStatusDTO.name())) {
            throw new EntityInvalidException(ENTITY_NAME, "name", createProposalStatusDTO.name(),
                    "Nome deve ter entre 1 e 255 caracteres e não pode estar vazio");
        }

        if (createProposalStatusDTO.code() != null && !isValidProposalStatusCode(createProposalStatusDTO.code())) {
            throw new EntityInvalidException(ENTITY_NAME, "code", createProposalStatusDTO.code(),
                    "Código deve ter no máximo 50 caracteres");
        }

        if (proposalStatusRepositoryImpl.existsByName(createProposalStatusDTO.name())) {
            throw new EntityAlreadyExistsException(ENTITY_NAME, "name", createProposalStatusDTO.name());
        }

        if (createProposalStatusDTO.code() != null && !createProposalStatusDTO.code().trim().isEmpty()) {
            if (proposalStatusRepository.existsByCode(createProposalStatusDTO.code())) {
                throw new EntityAlreadyExistsException(ENTITY_NAME, "code", createProposalStatusDTO.code());
            }
        }

        try {
            ProposalStatus proposalStatus = new ProposalStatus();
            proposalStatus.setName(createProposalStatusDTO.name());
            proposalStatus.setCode(createProposalStatusDTO.code());
            proposalStatus.setDescription(createProposalStatusDTO.description());
            proposalStatus.setColor(createProposalStatusDTO.color());
            proposalStatus.setSortOrder(createProposalStatusDTO.sortOrder() != null ? 
                    createProposalStatusDTO.sortOrder() : 0);
            proposalStatus.setIsInitial(createProposalStatusDTO.isInitial() != null ? 
                    createProposalStatusDTO.isInitial() : false);
            proposalStatus.setIsFinal(createProposalStatusDTO.isFinal() != null ? 
                    createProposalStatusDTO.isFinal() : false);

            return proposalStatusRepository.save(proposalStatus);
        } catch (Exception e) {
            throw new BusinessOperationException("createProposalStatus", ENTITY_NAME, null,
                    "Erro interno ao salvar: " + e.getMessage());
        }
    }

    public Optional<ProposalStatus> getProposalStatusById(Integer proposalStatusId) {
        if (proposalStatusId == null || proposalStatusId <= 0) {
            throw new EntityInvalidException(ENTITY_NAME, "id", String.valueOf(proposalStatusId),
                    "ID deve ser um número positivo");
        }

        return proposalStatusRepository.findById(proposalStatusId);
    }

    public ProposalStatus getProposalStatusByIdRequired(Integer proposalStatusId) {
        return getProposalStatusById(proposalStatusId)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, proposalStatusId));
    }

    public Optional<ProposalStatus> getProposalStatusByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new EntityInvalidException(ENTITY_NAME, "code", code,
                    "Código não pode estar vazio");
        }

        return proposalStatusRepository.findByCode(code);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public Page<ProposalStatus> getAllProposalStatuses(Pageable pageable) {
        try {
            return proposalStatusRepository.findByIsActiveTrue(pageable);
        } catch (Exception e) {
            throw new BusinessOperationException("getAllProposalStatuses",
                    "Erro ao buscar status de propostas: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public List<ProposalStatus> getAllActiveProposalStatuses() {
        try {
            return proposalStatusRepository.findAllOrderedBySort();
        } catch (Exception e) {
            throw new BusinessOperationException("getAllActiveProposalStatuses",
                    "Erro ao buscar status de propostas ativos: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProposalStatus updateProposalStatus(Integer proposalStatusId, UpdateProposalStatusDTO updateProposalStatusDTO) {
        ProposalStatus proposalStatus = getProposalStatusByIdRequired(proposalStatusId);

        if (!proposalStatus.getIsActive()) {
            throw new EntityDeactivatedException(ENTITY_NAME, proposalStatusId);
        }

        if (updateProposalStatusDTO.name() != null) {
            if (!isValidProposalStatusName(updateProposalStatusDTO.name())) {
                throw new EntityInvalidException(ENTITY_NAME, "name", updateProposalStatusDTO.name(),
                        "Nome deve ter entre 1 e 255 caracteres");
            }

            if (!updateProposalStatusDTO.name().equals(proposalStatus.getName()) &&
                    proposalStatusRepositoryImpl.existsByName(updateProposalStatusDTO.name())) {
                throw new EntityAlreadyExistsException(ENTITY_NAME, "name", updateProposalStatusDTO.name());
            }
        }

        if (updateProposalStatusDTO.code() != null) {
            if (!isValidProposalStatusCode(updateProposalStatusDTO.code())) {
                throw new EntityInvalidException(ENTITY_NAME, "code", updateProposalStatusDTO.code(),
                        "Código deve ter no máximo 50 caracteres");
            }

            if (!updateProposalStatusDTO.code().equals(proposalStatus.getCode()) &&
                    proposalStatusRepository.existsByCode(updateProposalStatusDTO.code())) {
                throw new EntityAlreadyExistsException(ENTITY_NAME, "code", updateProposalStatusDTO.code());
            }
        }

        try {
            if (updateProposalStatusDTO.name() != null) {
                proposalStatus.setName(updateProposalStatusDTO.name());
            }
            if (updateProposalStatusDTO.code() != null) {
                proposalStatus.setCode(updateProposalStatusDTO.code());
            }
            if (updateProposalStatusDTO.description() != null) {
                proposalStatus.setDescription(updateProposalStatusDTO.description());
            }
            if (updateProposalStatusDTO.color() != null) {
                proposalStatus.setColor(updateProposalStatusDTO.color());
            }
            if (updateProposalStatusDTO.sortOrder() != null) {
                proposalStatus.setSortOrder(updateProposalStatusDTO.sortOrder());
            }
            if (updateProposalStatusDTO.isInitial() != null) {
                proposalStatus.setIsInitial(updateProposalStatusDTO.isInitial());
            }
            if (updateProposalStatusDTO.isFinal() != null) {
                proposalStatus.setIsFinal(updateProposalStatusDTO.isFinal());
            }

            return proposalStatusRepository.save(proposalStatus);
        } catch (Exception e) {
            throw new BusinessOperationException("updateProposalStatus", ENTITY_NAME,
                    String.valueOf(proposalStatusId), "Erro ao atualizar: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProposalStatus(Integer proposalStatusId) {
        ProposalStatus proposalStatus = getProposalStatusByIdRequired(proposalStatusId);

        if (!proposalStatus.getIsActive()) {
            throw new EntityDeactivatedException(ENTITY_NAME, proposalStatusId);
        }

        try {
            proposalStatusRepositoryImpl.deactivateProposalStatus(proposalStatusId);
        } catch (Exception e) {
            throw new BusinessOperationException("deleteProposalStatus", ENTITY_NAME,
                    String.valueOf(proposalStatusId), "Erro ao desativar: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public List<ProposalStatus> searchProposalStatusesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EntityInvalidException(ENTITY_NAME, "name", name,
                    "Nome para busca não pode estar vazio");
        }

        try {
            return proposalStatusRepository.findByNameContainingIgnoreCase(name);
        } catch (Exception e) {
            throw new BusinessOperationException("searchProposalStatusesByName",
                    "Erro ao buscar status de propostas por nome: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public Page<ProposalStatus> getProposalStatusesWithFilters(String name, String code, Boolean isActive, 
                                                              Boolean isInitial, Boolean isFinal, 
                                                              Pageable pageable) {
        try {
            return proposalStatusRepositoryImpl.findProposalStatusesWithFilters(name, code, isActive, 
                    isInitial, isFinal, pageable);
        } catch (Exception e) {
            throw new BusinessOperationException("getProposalStatusesWithFilters",
                    "Erro ao buscar status de propostas com filtros: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean activateProposalStatus(Integer proposalStatusId) {
        ProposalStatus proposalStatus = getProposalStatusByIdRequired(proposalStatusId);

        if (proposalStatus.getIsActive()) {
            throw new BusinessOperationException("activateProposalStatus", ENTITY_NAME,
                    String.valueOf(proposalStatusId), "Status de proposta já está ativo");
        }

        try {
            return proposalStatusRepositoryImpl.activateProposalStatus(proposalStatusId);
        } catch (Exception e) {
            throw new BusinessOperationException("activateProposalStatus", ENTITY_NAME,
                    String.valueOf(proposalStatusId), "Erro ao ativar: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public boolean deactivateProposalStatus(Integer proposalStatusId) {
        ProposalStatus proposalStatus = getProposalStatusByIdRequired(proposalStatusId);

        if (!proposalStatus.getIsActive()) {
            throw new BusinessOperationException("deactivateProposalStatus", ENTITY_NAME,
                    String.valueOf(proposalStatusId), "Status de proposta já está desativado");
        }

        try {
            return proposalStatusRepositoryImpl.deactivateProposalStatus(proposalStatusId);
        } catch (Exception e) {
            throw new BusinessOperationException("deactivateProposalStatus", ENTITY_NAME,
                    String.valueOf(proposalStatusId), "Erro ao desativar: " + e.getMessage());
        }
    }

    public boolean existsByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return proposalStatusRepository.existsByCode(code);
    }

    public boolean existsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return proposalStatusRepositoryImpl.existsByName(name);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long countActiveProposalStatuses() {
        try {
            return proposalStatusRepository.countActiveProposalStatuses();
        } catch (Exception e) {
            throw new BusinessOperationException("countActiveProposalStatuses",
                    "Erro ao contar status de propostas ativas: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ProposalStatus> getRecentProposalStatuses(int days) {
        if (days < 0) {
            throw new EntityInvalidException(ENTITY_NAME, "days", String.valueOf(days),
                    "Número de dias deve ser positivo");
        }

        try {
            LocalDateTime startDate = LocalDateTime.now().minusDays(days);
            LocalDateTime endDate = LocalDateTime.now();
            return proposalStatusRepository.findProposalStatusesCreatedBetween(startDate, endDate);
        } catch (Exception e) {
            throw new BusinessOperationException("getRecentProposalStatuses",
                    "Erro ao buscar status de propostas recentes: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public List<ProposalStatus> getInitialStatuses() {
        try {
            return proposalStatusRepository.findInitialStatuses();
        } catch (Exception e) {
            throw new BusinessOperationException("getInitialStatuses",
                    "Erro ao buscar status iniciais: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public List<ProposalStatus> getFinalStatuses() {
        try {
            return proposalStatusRepository.findFinalStatuses();
        } catch (Exception e) {
            throw new BusinessOperationException("getFinalStatuses",
                    "Erro ao buscar status finais: " + e.getMessage());
        }
    }

    public ProposalStatusResponseDTO convertToResponseDTO(ProposalStatus proposalStatus) {
        if (proposalStatus == null) {
            throw new EntityInvalidException(ENTITY_NAME, "proposalStatus", "null",
                    "Status de proposta não pode ser nulo");
        }

        return new ProposalStatusResponseDTO(
                proposalStatus.getId(),
                proposalStatus.getName(),
                proposalStatus.getCode(),
                proposalStatus.getDescription(),
                proposalStatus.getColor(),
                proposalStatus.getSortOrder(),
                proposalStatus.getIsActive(),
                proposalStatus.getIsInitial(),
                proposalStatus.getIsFinal(),
                proposalStatus.getCreatedAt(),
                proposalStatus.getUpdatedAt()
        );
    }

    public List<ProposalStatusResponseDTO> convertToResponseDTOList(List<ProposalStatus> proposalStatuses) {
        if (proposalStatuses == null) {
            throw new EntityInvalidException(ENTITY_NAME, "proposalStatuses", "null",
                    "Lista de status de propostas não pode ser nula");
        }

        return proposalStatuses.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public boolean isValidProposalStatusCode(String code) {
        return code != null && !code.trim().isEmpty() && code.length() <= 50;
    }

    public boolean isValidProposalStatusName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 255;
    }
}
