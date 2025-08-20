package br.edu.infnet.victorapi.modules.proposals.services;

import br.edu.infnet.victorapi.modules.proposals.entity.Proposals;
import br.edu.infnet.victorapi.modules.proposals.repository.IProposalRepository;
import br.edu.infnet.victorapi.modules.proposals.repository.ProposalRepository;
import br.edu.infnet.victorapi.modules.proposals.dto.CreateProposalDTO;
import br.edu.infnet.victorapi.modules.proposals.dto.ProposalsFilterDTO;
import br.edu.infnet.victorapi.modules.proposals.dto.UpdateProposalDTO;
import br.edu.infnet.victorapi.modules.proposals.dto.ProposalResponseDTO;
import br.edu.infnet.victorapi.modules.proposals.dto.ConvertProposalToProjectDTO;
import br.edu.infnet.victorapi.modules.projects.dto.CreateProjectDTO;
import br.edu.infnet.victorapi.modules.projects.dto.ProjectResponseDTO;
import br.edu.infnet.victorapi.modules.projects.service.ProjectService;
import br.edu.infnet.victorapi.exceptions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProposalService {

    private static final String ENTITY_NAME = "Proposta";

    @PersistenceContext
    private EntityManager entityManager;

    private final IProposalRepository proposalRepository;
    private final ProposalRepository proposalRepositoryImpl;
    private final ProjectService projectService;

    @Autowired
    public ProposalService(IProposalRepository proposalRepository, 
                          ProposalRepository proposalRepositoryImpl,
                          ProjectService projectService) {
        this.proposalRepository = proposalRepository;
        this.proposalRepositoryImpl = proposalRepositoryImpl;
        this.projectService = projectService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public Proposals createProposal(CreateProposalDTO createProposalDTO) {
        if (!isValidProposalName(createProposalDTO.name())) {
            throw new EntityInvalidException(ENTITY_NAME, "name", createProposalDTO.name(),
                    "Nome deve ter entre 1 e 255 caracteres e não pode estar vazio");
        }

        if (createProposalDTO.proposalNumber() != null && 
            proposalRepositoryImpl.existsByProposalNumber(createProposalDTO.proposalNumber())) {
            throw new EntityAlreadyExistsException(ENTITY_NAME, "proposalNumber", createProposalDTO.proposalNumber());
        }

        try {
            Proposals proposal = new Proposals();
            proposal.setName(createProposalDTO.name());
            proposal.setDescription(createProposalDTO.description());
            proposal.setProposalNumber(createProposalDTO.proposalNumber());
            proposal.setSite(createProposalDTO.site());
            proposal.setValue(createProposalDTO.value());
            proposal.setSchedule(createProposalDTO.schedule());
            proposal.setIbt(createProposalDTO.ibt());
            proposal.setPaymentDays(createProposalDTO.paymentDays());
            proposal.setEstimatedStart(createProposalDTO.estimatedStart());
            proposal.setProbability(createProposalDTO.probability());
            proposal.setProposalSubNumber(createProposalDTO.proposalSubNumber());
            proposal.setExchangeRate(createProposalDTO.exchangeRate() != null ? 
                    createProposalDTO.exchangeRate() : BigDecimal.ONE);
            proposal.setCompanyName(createProposalDTO.companyName());
            proposal.setPriority(createProposalDTO.priority() != null ? 
                    createProposalDTO.priority() : 0);
            proposal.setDueDays(createProposalDTO.dueDays());

            // Set related entities
            setRelatedEntities(proposal, createProposalDTO.departmentId(), createProposalDTO.sectorId(),
                    createProposalDTO.areaId(), createProposalDTO.clientSupplierId(),
                    createProposalDTO.contractId(), createProposalDTO.officeId(),
                    createProposalDTO.coinTypeId(), createProposalDTO.countryId(),
                    createProposalDTO.statusId(), createProposalDTO.responsibleId(),
                    createProposalDTO.originProposalId());

            return proposalRepository.save(proposal);
        } catch (Exception e) {
            throw new BusinessOperationException("createProposal", ENTITY_NAME, null,
                    "Erro interno ao salvar: " + e.getMessage());
        }
    }

    public Optional<Proposals> getProposalById(Integer proposalId) {
        if (proposalId == null || proposalId <= 0) {
            throw new EntityInvalidException(ENTITY_NAME, "id", String.valueOf(proposalId),
                    "ID deve ser um número positivo");
        }

        return proposalRepository.findById(proposalId);
    }

    public Proposals getProposalByIdRequired(Integer proposalId) {
        return getProposalById(proposalId)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, proposalId));
    }

    public Optional<Proposals> getProposalByNumber(String proposalNumber) {
        if (proposalNumber == null || proposalNumber.trim().isEmpty()) {
            throw new EntityInvalidException(ENTITY_NAME, "proposalNumber", proposalNumber,
                    "Número da proposta não pode estar vazio");
        }

        return proposalRepository.findByProposalNumber(proposalNumber);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_USER')")
    public Page<Proposals> getAllProposals(Pageable pageable) {
        try {
            return proposalRepository.findAllOrderByCreatedAtDesc(pageable);
        } catch (Exception e) {
            throw new BusinessOperationException("getAllProposals",
                    "Erro ao buscar propostas: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public Proposals updateProposal(Integer proposalId, UpdateProposalDTO updateProposalDTO) {
        Proposals proposal = getProposalByIdRequired(proposalId);

        if (updateProposalDTO.name() != null) {
            if (!isValidProposalName(updateProposalDTO.name())) {
                throw new EntityInvalidException(ENTITY_NAME, "name", updateProposalDTO.name(),
                        "Nome deve ter entre 1 e 255 caracteres");
            }
        }

        if (updateProposalDTO.proposalNumber() != null &&
            !updateProposalDTO.proposalNumber().equals(proposal.getProposalNumber()) &&
            proposalRepositoryImpl.existsByProposalNumber(updateProposalDTO.proposalNumber())) {
            throw new EntityAlreadyExistsException(ENTITY_NAME, "proposalNumber", updateProposalDTO.proposalNumber());
        }

        try {
            // Update basic fields
            if (updateProposalDTO.name() != null) {
                proposal.setName(updateProposalDTO.name());
            }
            if (updateProposalDTO.description() != null) {
                proposal.setDescription(updateProposalDTO.description());
            }
            if (updateProposalDTO.proposalNumber() != null) {
                proposal.setProposalNumber(updateProposalDTO.proposalNumber());
            }
            if (updateProposalDTO.site() != null) {
                proposal.setSite(updateProposalDTO.site());
            }
            if (updateProposalDTO.value() != null) {
                proposal.setValue(updateProposalDTO.value());
            }
            if (updateProposalDTO.schedule() != null) {
                proposal.setSchedule(updateProposalDTO.schedule());
            }
            if (updateProposalDTO.ibt() != null) {
                proposal.setIbt(updateProposalDTO.ibt());
            }
            if (updateProposalDTO.paymentDays() != null) {
                proposal.setPaymentDays(updateProposalDTO.paymentDays());
            }
            if (updateProposalDTO.estimatedStart() != null) {
                proposal.setEstimatedStart(updateProposalDTO.estimatedStart());
            }
            if (updateProposalDTO.probability() != null) {
                proposal.setProbability(updateProposalDTO.probability());
            }
            if (updateProposalDTO.proposalSubNumber() != null) {
                proposal.setProposalSubNumber(updateProposalDTO.proposalSubNumber());
            }
            if (updateProposalDTO.exchangeRate() != null) {
                proposal.setExchangeRate(updateProposalDTO.exchangeRate());
            }
            if (updateProposalDTO.companyName() != null) {
                proposal.setCompanyName(updateProposalDTO.companyName());
            }
            if (updateProposalDTO.priority() != null) {
                proposal.setPriority(updateProposalDTO.priority());
            }
            if (updateProposalDTO.dueDays() != null) {
                proposal.setDueDays(updateProposalDTO.dueDays());
            }

            // Update related entities
            updateRelatedEntities(proposal, updateProposalDTO.departmentId(), updateProposalDTO.sectorId(),
                    updateProposalDTO.areaId(), updateProposalDTO.clientSupplierId(),
                    updateProposalDTO.contractId(), updateProposalDTO.officeId(),
                    updateProposalDTO.coinTypeId(), updateProposalDTO.countryId(),
                    updateProposalDTO.statusId(), updateProposalDTO.responsibleId(),
                    updateProposalDTO.originProposalId());

            return proposalRepository.save(proposal);
        } catch (Exception e) {
            throw new BusinessOperationException("updateProposal", ENTITY_NAME,
                    String.valueOf(proposalId), "Erro ao atualizar: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteProposal(Integer proposalId) {
        getProposalByIdRequired(proposalId); // Validate existence

        try {
            proposalRepositoryImpl.deleteById(proposalId);
        } catch (Exception e) {
            throw new BusinessOperationException("deleteProposal", ENTITY_NAME,
                    String.valueOf(proposalId), "Erro ao deletar: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_USER')")
    public List<Proposals> searchProposalsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new EntityInvalidException(ENTITY_NAME, "name", name,
                    "Nome para busca não pode estar vazio");
        }

        try {
            return proposalRepository.findByNameContainingIgnoreCase(name);
        } catch (Exception e) {
            throw new BusinessOperationException("searchProposalsByName",
                    "Erro ao buscar propostas por nome: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_USER')")
    public Page<Proposals> getProposalsWithFilters(ProposalsFilterDTO filterDTO, Pageable pageable) {
        try {
            return proposalRepositoryImpl.findProposalsWithFilters(
                    filterDTO.name(), 
                    filterDTO.proposalNumber(), 
                    filterDTO.departmentId(),
                    filterDTO.sectorId(), 
                    filterDTO.statusId(), 
                    filterDTO.responsibleId(),
                    filterDTO.minValue(), 
                    filterDTO.maxValue(),
                    filterDTO.startDate(), 
                    filterDTO.endDate(),
                    filterDTO.priority(), 
                    pageable);
        } catch (Exception e) {
            throw new BusinessOperationException("getProposalsWithFilters",
                    "Erro ao buscar propostas com filtros: " + e.getMessage());
        }
    }

    public boolean existsByProposalNumber(String proposalNumber) {
        if (proposalNumber == null || proposalNumber.trim().isEmpty()) {
            return false;
        }
        return proposalRepository.existsByProposalNumber(proposalNumber);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public Long countAllProposals() {
        try {
            return proposalRepository.countAllProposals();
        } catch (Exception e) {
            throw new BusinessOperationException("countAllProposals",
                    "Erro ao contar propostas: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public BigDecimal getTotalValue() {
        try {
            return proposalRepository.sumAllProposalValues();
        } catch (Exception e) {
            throw new BusinessOperationException("getTotalValue",
                    "Erro ao calcular valor total das propostas: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    public List<Proposals> getRecentProposals(int days) {
        if (days < 0) {
            throw new EntityInvalidException(ENTITY_NAME, "days", String.valueOf(days),
                    "Número de dias deve ser positivo");
        }

        try {
            LocalDateTime startDate = LocalDateTime.now().minusDays(days);
            LocalDateTime endDate = LocalDateTime.now();
            return proposalRepository.findProposalsCreatedBetween(startDate, endDate);
        } catch (Exception e) {
            throw new BusinessOperationException("getRecentProposals",
                    "Erro ao buscar propostas recentes: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_USER')")
    public List<Proposals> getProposalsByResponsible(Integer responsibleId) {
        try {
            return proposalRepository.findByResponsibleId(responsibleId);
        } catch (Exception e) {
            throw new BusinessOperationException("getProposalsByResponsible",
                    "Erro ao buscar propostas por responsável: " + e.getMessage());
        }
    }

    public ProposalResponseDTO convertToResponseDTO(Proposals proposal) {
        if (proposal == null) {
            throw new EntityInvalidException(ENTITY_NAME, "proposal", "null",
                    "Proposta não pode ser nula");
        }

        ProposalResponseDTO dto = new ProposalResponseDTO();
        dto.setId(proposal.getId());
        dto.setName(proposal.getName());
        dto.setDescription(proposal.getDescription());
        dto.setProposalNumber(proposal.getProposalNumber());
        dto.setSite(proposal.getSite());
        dto.setValue(proposal.getValue());
        dto.setSchedule(proposal.getSchedule());
        dto.setIbt(proposal.getIbt());
        dto.setPaymentDays(proposal.getPaymentDays());
        dto.setEstimatedStart(proposal.getEstimatedStart());
        dto.setProbability(proposal.getProbability());
        dto.setProposalSubNumber(proposal.getProposalSubNumber());
        dto.setExchangeRate(proposal.getExchangeRate());
        dto.setCompanyName(proposal.getCompanyName());
        dto.setPriority(proposal.getPriority());
        dto.setDueDays(proposal.getDueDays());
        dto.setCreatedAt(proposal.getCreatedAt());
        dto.setUpdatedAt(proposal.getUpdatedAt());

        dto.setDepartmentId(proposal.getDepartmentId());
        dto.setSectorId(proposal.getSectorId());
        dto.setAreaId(proposal.getAreaId());
        dto.setClientSupplierId(proposal.getClientSupplierId());
        dto.setContractId(proposal.getContractId());
        dto.setOfficeId(proposal.getOfficeId());
        dto.setCoinTypeId(proposal.getCoinTypeId());
        dto.setCountryId(proposal.getCountryId());
        dto.setStatusId(proposal.getStatusId());
        dto.setResponsibleId(proposal.getResponsibleId());
        dto.setOriginProposalId(proposal.getOriginProposalId());

        return dto;
    }

    public List<ProposalResponseDTO> convertToResponseDTOList(List<Proposals> proposals) {
        if (proposals == null) {
            throw new EntityInvalidException(ENTITY_NAME, "proposals", "null",
                    "Lista de propostas não pode ser nula");
        }

        return proposals.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    private void setRelatedEntities(Proposals proposal, Integer departmentId, Integer sectorId,
                                   Integer areaId, Integer clientSupplierId, Integer contractId,
                                   Integer officeId, Integer coinTypeId, Integer countryId,
                                   Integer statusId, Integer responsibleId, Integer originProposalId) {
        
        if (departmentId != null) {
            proposal.setDepartmentId(departmentId);
        }

        if (sectorId != null) {
            proposal.setSectorId(sectorId);
        }

        if (areaId != null) {
            proposal.setAreaId(areaId);
        }

        if (clientSupplierId != null) {
            proposal.setClientSupplierId(clientSupplierId);
        }

        if (contractId != null) {
            proposal.setContractId(contractId);
        }

        if (officeId != null) {
            proposal.setOfficeId(officeId);
        }

        if (coinTypeId != null) {
            proposal.setCoinTypeId(coinTypeId);
        }

        if (countryId != null) {
            proposal.setCountryId(countryId);
        }

        if (statusId != null) {
            proposal.setStatusId(statusId);
        }

        if (responsibleId != null) {
            proposal.setResponsibleId(responsibleId);
        }

        if (originProposalId != null) {
            proposal.setOriginProposalId(originProposalId);
        }
    }

    private void updateRelatedEntities(Proposals proposal, Integer departmentId, Integer sectorId,
                                      Integer areaId, Integer clientSupplierId, Integer contractId,
                                      Integer officeId, Integer coinTypeId, Integer countryId,
                                      Integer statusId, Integer responsibleId, Integer originProposalId) {
        
        if (departmentId != null) {
            proposal.setDepartmentId(departmentId);
        }

        if (sectorId != null) {
            proposal.setSectorId(sectorId);
        }

        if (areaId != null) {
            proposal.setAreaId(areaId);
        }

        if (clientSupplierId != null) {
            proposal.setClientSupplierId(clientSupplierId);
        }

        if (contractId != null) {
            proposal.setContractId(contractId);
        }

        if (officeId != null) {
            proposal.setOfficeId(officeId);
        }

        if (coinTypeId != null) {
            proposal.setCoinTypeId(coinTypeId);
        }

        if (countryId != null) {
            proposal.setCountryId(countryId);
        }

        if (statusId != null) {
            proposal.setStatusId(statusId);
        }

        if (responsibleId != null) {
            proposal.setResponsibleId(responsibleId);
        }

        if (originProposalId != null) {
            proposal.setOriginProposalId(originProposalId);
        }
    }

    public boolean isValidProposalName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() <= 255;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @Transactional
    public ProjectResponseDTO convertProposalToProject(Integer proposalId, ConvertProposalToProjectDTO convertDTO) {
        // Buscar a proposta
        Proposals proposal = getProposalByIdRequired(proposalId);

        try {
            // Mapear campos da proposta para o projeto
            CreateProjectDTO createProjectDTO = new CreateProjectDTO(
                    // Nome do projeto (usar o fornecido ou o da proposta)
                    convertDTO.projectName() != null ? convertDTO.projectName() : proposal.getName(),
                    
                    // Descrição combinada
                    combineDescriptions(proposal.getDescription(), convertDTO.additionalDescription()),
                    
                    // Mapear departmentId (mesmo campo)
                    proposal.getDepartmentId(),
                    
                    // ProjectType - usar o fornecido ou um padrão
                    convertDTO.projectTypeId() != null ? convertDTO.projectTypeId() : 1,
                    
                    // Mapear sectorsId (mesmo campo)
                    proposal.getSectorId(),
                    
                    // Mapear areasId (mesmo campo)
                    proposal.getAreaId(),
                    
                    // Status inicial do projeto - usar o fornecido ou padrão
                    convertDTO.initialProjectStatusId() != null ? convertDTO.initialProjectStatusId() : 1,
                    
                    // originProjectsId - null para novo projeto
                    null,
                    
                    // Mapear countriesId (mesmo campo)
                    proposal.getCountryId(),
                    
                    // Mapear clientsSuppliersId (mesmo campo)
                    proposal.getClientSupplierId(),
                    
                    // lastProjectStatusesId - mesmo que o status inicial
                    convertDTO.initialProjectStatusId() != null ? convertDTO.initialProjectStatusId() : 1,
                    
                    // Mapear coinTypeId (mesmo campo)
                    proposal.getCoinTypeId(),
                    
                    // Referenciar a proposta original
                    proposalId,
                    
                    // Flags booleanas com valores padrão ou fornecidos
                    convertDTO.billable() != null ? convertDTO.billable() : false,
                    convertDTO.international() != null ? convertDTO.international() : false,
                    
                    // Diretório do projeto
                    convertDTO.projectDirectory(),
                    
                    // Site (mesmo campo)
                    proposal.getSite(),
                    
                    // isDefault - false para projetos convertidos
                    false,
                    
                    // exchangeRate (mesmo campo)
                    proposal.getExchangeRate(),
                    
                    // openingEmail
                    convertDTO.openingEmail(),
                    
                    // classification
                    convertDTO.classification(),
                    
                    // Flags adicionais
                    convertDTO.investment() != null ? convertDTO.investment() : false,
                    convertDTO.product() != null ? convertDTO.product() : false
            );

            // Criar o projeto usando o ProjectService
            ProjectResponseDTO projectResponse = projectService.create(createProjectDTO);

            return projectResponse;

        } catch (Exception e) {
            throw new BusinessOperationException("convertProposalToProject", ENTITY_NAME,
                    String.valueOf(proposalId), "Erro ao converter proposta em projeto: " + e.getMessage());
        }
    }

    private String combineDescriptions(String proposalDescription, String additionalDescription) {
        StringBuilder combined = new StringBuilder();
        
        if (proposalDescription != null && !proposalDescription.trim().isEmpty()) {
            combined.append(proposalDescription);
        }
        
        if (additionalDescription != null && !additionalDescription.trim().isEmpty()) {
            if (combined.length() > 0) {
                combined.append("\n\n");
            }
            combined.append(additionalDescription);
        }
        
        return combined.length() > 0 ? combined.toString() : null;
    }
}
