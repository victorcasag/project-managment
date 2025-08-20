package br.edu.infnet.victorapi.modules.countries.services;

import br.edu.infnet.victorapi.exceptions.EntityAlreadyExistsException;
import br.edu.infnet.victorapi.exceptions.EntityNotFoundException;
import br.edu.infnet.victorapi.modules.countries.dto.CountryFilterDTO;
import br.edu.infnet.victorapi.modules.countries.dto.CountryResponseDTO;
import br.edu.infnet.victorapi.modules.countries.dto.CreateCountryDTO;
import br.edu.infnet.victorapi.modules.countries.dto.UpdateCountryDTO;
import br.edu.infnet.victorapi.modules.countries.entity.Country;
import br.edu.infnet.victorapi.modules.countries.repository.CountryRepository;
import br.edu.infnet.victorapi.modules.countries.repository.ICountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    @Autowired
    private ICountryRepository countryRepository;

    @Autowired
    private CountryRepository customCountryRepository;

    @PreAuthorize("hasRole('USER')")
    public List<CountryResponseDTO> findAll() {
        List<Country> countries = countryRepository.findAllOrderByName();
        return countries.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public Page<CountryResponseDTO> findAll(Pageable pageable) {
        Page<Country> countries = countryRepository.findAllOrderByName(pageable);
        return countries.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public Page<CountryResponseDTO> findWithFilters(CountryFilterDTO filters, Pageable pageable) {
        Page<Country> countries = customCountryRepository.findCountriesWithFilters(
                filters.name(), filters.code2(), filters.code3(), 
                filters.currencyCode(), filters.isActive(), pageable);
        return countries.map(this::toDTO);
    }

    @PreAuthorize("hasRole('USER')")
    public CountryResponseDTO findById(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country", id));
        return toDTO(country);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CountryResponseDTO create(CreateCountryDTO dto) {
        if (dto.code2() != null && countryRepository.existsByCode2(dto.code2())) {
            throw new EntityAlreadyExistsException("Country", "País com este código 2 já existe");
        }

        if (dto.code3() != null && countryRepository.existsByCode3(dto.code3())) {
            throw new EntityAlreadyExistsException("Country", "País com este código 3 já existe");
        }

        if (countryRepository.countByName(dto.name()) > 0) {
            throw new EntityAlreadyExistsException("Country", "País com este nome já existe");
        }

        Country country = new Country();
        country.setName(dto.name());
        country.setCode2(dto.code2());
        country.setCode3(dto.code3());
        country.setCurrencyCode(dto.currencyCode());
        country.setIsActive(true);

        country = countryRepository.save(country);
        return toDTO(country);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CountryResponseDTO update(Integer id, UpdateCountryDTO dto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country", id));

        if (dto.name() != null) {
            country.setName(dto.name());
        }
        if (dto.code2() != null) {
            country.setCode2(dto.code2());
        }
        if (dto.code3() != null) {
            country.setCode3(dto.code3());
        }
        if (dto.currencyCode() != null) {
            country.setCurrencyCode(dto.currencyCode());
        }
        if (dto.isActive() != null) {
            country.setIsActive(dto.isActive());
        }

        country = countryRepository.save(country);
        return toDTO(country);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void delete(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country", id));

        country.setIsActive(false);
        countryRepository.save(country);
    }

    @PreAuthorize("hasRole('USER')")
    public List<CountryResponseDTO> findActive() {
        List<Country> countries = countryRepository.findAllActive();
        return countries.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public List<CountryResponseDTO> findByCurrencyCode(String currencyCode) {
        List<Country> countries = countryRepository.findByCurrencyCode(currencyCode);
        return countries.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER')")
    public CountryResponseDTO findByCode2(String code2) {
        Country country = countryRepository.findByCode2(code2)
                .orElseThrow(() -> new EntityNotFoundException("Country", "Código 2: " + code2));
        return toDTO(country);
    }

    @PreAuthorize("hasRole('USER')")
    public CountryResponseDTO findByCode3(String code3) {
        Country country = countryRepository.findByCode3(code3)
                .orElseThrow(() -> new EntityNotFoundException("Country", "Código 3: " + code3));
        return toDTO(country);
    }

    private CountryResponseDTO toDTO(Country country) {
        return new CountryResponseDTO(
                country.getId(),
                country.getName(),
                country.getCode2(),
                country.getCode3(),
                country.getCurrencyCode(),
                country.getIsActive(),
                country.getCreatedAt(),
                country.getUpdatedAt()
        );
    }
}
