package br.edu.infnet.victorapi.modules.cointype.services;

import br.edu.infnet.victorapi.exceptions.EntityAlreadyExistsException;
import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.cointype.dto.CoinTypeFilterDTO;
import br.edu.infnet.victorapi.modules.cointype.dto.CoinTypeResponseDTO;
import br.edu.infnet.victorapi.modules.cointype.dto.CreateCoinTypeDTO;
import br.edu.infnet.victorapi.modules.cointype.dto.UpdateCoinTypeDTO;
import br.edu.infnet.victorapi.modules.cointype.entity.CoinType;
import br.edu.infnet.victorapi.modules.cointype.repository.CoinTypeRepository;
import br.edu.infnet.victorapi.modules.cointype.repository.ICoinTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinTypeService {

    @Autowired
    private ICoinTypeRepository coinTypeRepository;

    @Autowired
    private CoinTypeRepository customCoinTypeRepository;

    @PreAuthorize("hasRole('USER')")
    public List<CoinTypeResponseDTO> findAll() {
        List<CoinType> coinTypes = coinTypeRepository.findAllOrderByName();
        return coinTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public Page<CoinTypeResponseDTO> findAll(Pageable pageable) {
        Page<CoinType> coinTypes = coinTypeRepository.findAllOrderByName(pageable);
        return coinTypes.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public Page<CoinTypeResponseDTO> findWithFilters(CoinTypeFilterDTO filters, Pageable pageable) {
        Page<CoinType> coinTypes = customCoinTypeRepository.findCoinTypesWithFilters(
                filters.name(), filters.code(), filters.symbol(), filters.isActive(), pageable);
        return coinTypes.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public CoinTypeResponseDTO findById(Integer id) {
        CoinType coinType = coinTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CoinType", id));
        return toDTO(coinType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CoinTypeResponseDTO create(CreateCoinTypeDTO dto) {
        if (dto.code() != null && coinTypeRepository.existsByCode(dto.code())) {
            throw new EntityAlreadyExistsException("CoinType", "Tipo de moeda com este código já existe");
        }

        if (coinTypeRepository.countByName(dto.name()) > 0) {
            throw new EntityAlreadyExistsException("CoinType", "Tipo de moeda com este nome já existe");
        }

        CoinType coinType = new CoinType();
        coinType.setName(dto.name());
        coinType.setCode(dto.code());
        coinType.setSymbol(dto.symbol());
        coinType.setIsActive(true);

        coinType = coinTypeRepository.save(coinType);
        return toDTO(coinType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CoinTypeResponseDTO update(Integer id, UpdateCoinTypeDTO dto) {
        CoinType coinType = coinTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CoinType", id));

        if (dto.name() != null) {
            coinType.setName(dto.name());
        }
        if (dto.code() != null) {
            coinType.setCode(dto.code());
        }
        if (dto.symbol() != null) {
            coinType.setSymbol(dto.symbol());
        }
        if (dto.isActive() != null) {
            coinType.setIsActive(dto.isActive());
        }

        coinType = coinTypeRepository.save(coinType);
        return toDTO(coinType);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Integer id) {
        CoinType coinType = coinTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CoinType", id));

        coinType.setIsActive(false);
        coinTypeRepository.save(coinType);
    }

    @PreAuthorize("hasRole('USER')")
    public List<CoinTypeResponseDTO> findActive() {
        List<CoinType> coinTypes = coinTypeRepository.findAllActive();
        return coinTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<CoinTypeResponseDTO> findBySymbol(String symbol) {
        List<CoinType> coinTypes = coinTypeRepository.findBySymbol(symbol);
        return coinTypes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public CoinTypeResponseDTO findByCode(String code) {
        CoinType coinType = coinTypeRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("CoinType", "Código: " + code));
        return toDTO(coinType);
    }

    private CoinTypeResponseDTO toDTO(CoinType coinType) {
        return new CoinTypeResponseDTO(
                coinType.getId(),
                coinType.getName(),
                coinType.getCode(),
                coinType.getSymbol(),
                coinType.getIsActive(),
                coinType.getCreatedAt(),
                coinType.getUpdatedAt()
        );
    }
}
