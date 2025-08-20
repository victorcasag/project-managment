package br.edu.infnet.victorapi.modules.countries.controller;

import br.edu.infnet.victorapi.modules.countries.dto.CountryFilterDTO;
import br.edu.infnet.victorapi.modules.countries.dto.CountryResponseDTO;
import br.edu.infnet.victorapi.modules.countries.dto.CreateCountryDTO;
import br.edu.infnet.victorapi.modules.countries.dto.UpdateCountryDTO;
import br.edu.infnet.victorapi.modules.countries.services.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@Tag(name = "Countries", description = "Gerenciamento de países")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @Operation(summary = "Listar todos os países", description = "Retorna uma lista paginada de todos os países")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de países retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<Page<CountryResponseDTO>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<CountryResponseDTO> countries = countryService.findAll(pageable);
        return ResponseEntity.ok(countries);
    }

    @Operation(summary = "Buscar países com filtros", description = "Retorna uma lista paginada de países com filtros aplicados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de países retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<CountryResponseDTO>> findWithFilters(
            @Parameter(description = "Nome do país") @RequestParam(required = false) String name,
            @Parameter(description = "Código de 2 letras") @RequestParam(required = false) String code2,
            @Parameter(description = "Código de 3 letras") @RequestParam(required = false) String code3,
            @Parameter(description = "Código da moeda") @RequestParam(required = false) String currencyCode,
            @Parameter(description = "Status ativo") @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        
        CountryFilterDTO filters = new CountryFilterDTO(
                name, code2, code3, currencyCode, isActive
        );
        
        Page<CountryResponseDTO> countries = countryService.findWithFilters(filters, pageable);
        return ResponseEntity.ok(countries);
    }

    @Operation(summary = "Buscar país por ID", description = "Retorna um país específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CountryResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "País não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CountryResponseDTO> findById(@PathVariable Integer id) {
        CountryResponseDTO country = countryService.findById(id);
        return ResponseEntity.ok(country);
    }

    @Operation(summary = "Listar países ativos", description = "Retorna uma lista de países ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de países ativos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/active")
    public ResponseEntity<List<CountryResponseDTO>> findActive() {
        List<CountryResponseDTO> countries = countryService.findActive();
        return ResponseEntity.ok(countries);
    }

    @Operation(summary = "Buscar país por código de 2 letras", description = "Retorna um país pelo código de 2 letras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "País não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/code2/{code2}")
    public ResponseEntity<CountryResponseDTO> findByCode2(@PathVariable String code2) {
        CountryResponseDTO country = countryService.findByCode2(code2);
        return ResponseEntity.ok(country);
    }

    @Operation(summary = "Buscar país por código de 3 letras", description = "Retorna um país pelo código de 3 letras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "País não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/code3/{code3}")
    public ResponseEntity<CountryResponseDTO> findByCode3(@PathVariable String code3) {
        CountryResponseDTO country = countryService.findByCode3(code3);
        return ResponseEntity.ok(country);
    }

    @Operation(summary = "Buscar países por código de moeda", description = "Retorna países que usam uma moeda específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de países retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/currency/{currencyCode}")
    public ResponseEntity<List<CountryResponseDTO>> findByCurrencyCode(@PathVariable String currencyCode) {
        List<CountryResponseDTO> countries = countryService.findByCurrencyCode(currencyCode);
        return ResponseEntity.ok(countries);
    }

    @Operation(summary = "Criar novo país", description = "Cria um novo país no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "País criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CountryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "País já existe"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<CountryResponseDTO> create(@Valid @RequestBody CreateCountryDTO dto) {
        CountryResponseDTO country = countryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(country);
    }

    @Operation(summary = "Atualizar país", description = "Atualiza um país existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "País atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = CountryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "País não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CountryResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody UpdateCountryDTO dto) {
        CountryResponseDTO country = countryService.update(id, dto);
        return ResponseEntity.ok(country);
    }

    @Operation(summary = "Desativar país", description = "Desativa um país (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "País desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "País não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        countryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
