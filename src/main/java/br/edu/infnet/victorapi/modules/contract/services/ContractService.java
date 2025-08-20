package br.edu.infnet.victorapi.modules.contract.services;

import br.edu.infnet.victorapi.exceptions.EntityAlreadyExistsException;
import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.contract.dto.ContractFilterDTO;
import br.edu.infnet.victorapi.modules.contract.dto.ContractResponseDTO;
import br.edu.infnet.victorapi.modules.contract.dto.CreateContractDTO;
import br.edu.infnet.victorapi.modules.contract.dto.UpdateContractDTO;
import br.edu.infnet.victorapi.modules.contract.entity.Contract;
import br.edu.infnet.victorapi.modules.contract.repository.ContractRepository;
import br.edu.infnet.victorapi.modules.contract.repository.IContractRepository;
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
public class ContractService {

    @Autowired
    private IContractRepository contractRepository;

    @Autowired
    private ContractRepository customContractRepository;

    @PreAuthorize("hasRole('USER')")
    public List<ContractResponseDTO> findAll() {
        List<Contract> contracts = contractRepository.findAllOrderByName();
        return contracts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public Page<ContractResponseDTO> findAll(Pageable pageable) {
        Page<Contract> contracts = contractRepository.findAllOrderByName(pageable);
        return contracts.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public Page<ContractResponseDTO> findWithFilters(ContractFilterDTO filters, Pageable pageable) {
        Page<Contract> contracts = customContractRepository.findContractsWithFilters(
                filters.name(), filters.contractNumber(), filters.description(), 
                filters.clientSupplierId(), filters.coinTypeId(), filters.startDateFrom(), 
                filters.startDateTo(), filters.endDateFrom(), filters.endDateTo(), 
                filters.valueFrom(), filters.valueTo(), filters.isActive(), pageable);
        return contracts.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public ContractResponseDTO findById(Integer id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract", id));
        return toDTO(contract);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ContractResponseDTO create(CreateContractDTO dto) {
        if (dto.contractNumber() != null && contractRepository.existsByContractNumber(dto.contractNumber())) {
            throw new EntityAlreadyExistsException("Contract", "Contrato com este número já existe");
        }

        if (contractRepository.countByName(dto.name()) > 0) {
            throw new EntityAlreadyExistsException("Contract", "Contrato com este nome já existe");
        }

        Contract contract = new Contract();
        contract.setName(dto.name());
        contract.setDescription(dto.description());
        contract.setContractNumber(dto.contractNumber());
        contract.setStartDate(dto.startDate());
        contract.setEndDate(dto.endDate());
        contract.setValue(dto.value());
        contract.setCoinTypeId(dto.coinTypeId());
        contract.setClientSupplierId(dto.clientSupplierId());
        contract.setIsActive(true);

        contract = contractRepository.save(contract);
        return toDTO(contract);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ContractResponseDTO update(Integer id, UpdateContractDTO dto) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract", id));

        if (dto.name() != null) {
            contract.setName(dto.name());
        }
        if (dto.description() != null) {
            contract.setDescription(dto.description());
        }
        if (dto.contractNumber() != null) {
            contract.setContractNumber(dto.contractNumber());
        }
        if (dto.startDate() != null) {
            contract.setStartDate(dto.startDate());
        }
        if (dto.endDate() != null) {
            contract.setEndDate(dto.endDate());
        }
        if (dto.value() != null) {
            contract.setValue(dto.value());
        }
        if (dto.coinTypeId() != null) {
            contract.setCoinTypeId(dto.coinTypeId());
        }
        if (dto.clientSupplierId() != null) {
            contract.setClientSupplierId(dto.clientSupplierId());
        }
        if (dto.isActive() != null) {
            contract.setIsActive(dto.isActive());
        }

        contract = contractRepository.save(contract);
        return toDTO(contract);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Integer id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contract", id));

        contract.setIsActive(false);
        contractRepository.save(contract);
    }

    @PreAuthorize("hasRole('USER')")
    public List<ContractResponseDTO> findActive() {
        List<Contract> contracts = contractRepository.findAllActive();
        return contracts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public ContractResponseDTO findByContractNumber(String contractNumber) {
        Contract contract = contractRepository.findByContractNumber(contractNumber)
                .orElseThrow(() -> new EntityNotFoundException("Contract", "Número: " + contractNumber));
        return toDTO(contract);
    }

    @PreAuthorize("hasRole('USER')")
    public List<ContractResponseDTO> findByClientSupplier(Integer clientSupplierId) {
        List<Contract> contracts = contractRepository.findByClientSupplierId(clientSupplierId);
        return contracts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<ContractResponseDTO> findByCoinType(Integer coinTypeId) {
        List<Contract> contracts = contractRepository.findByCoinTypeId(coinTypeId);
        return contracts.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public BigDecimal getTotalActiveValue() {
        BigDecimal total = contractRepository.sumTotalActiveValue();
        return total != null ? total : BigDecimal.ZERO;
    }

    private ContractResponseDTO toDTO(Contract contract) {
        return new ContractResponseDTO(
                contract.getId(),
                contract.getName(),
                contract.getDescription(),
                contract.getContractNumber(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getValue(),
                contract.getCoinTypeId(),
                contract.getClientSupplierId(),
                contract.getIsActive(),
                contract.getCreatedAt(),
                contract.getUpdatedAt()
        );
    }
}
