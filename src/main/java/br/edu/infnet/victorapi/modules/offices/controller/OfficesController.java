package br.edu.infnet.victorapi.modules.offices.controller;

import br.edu.infnet.victorapi.modules.offices.dto.CreateOfficesDTO;
import br.edu.infnet.victorapi.modules.offices.dto.OfficesFilterDTO;
import br.edu.infnet.victorapi.modules.offices.dto.OfficesResponseDTO;
import br.edu.infnet.victorapi.modules.offices.dto.UpdateOfficesDTO;
import br.edu.infnet.victorapi.modules.offices.services.OfficesService;
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
@RequestMapping("/api/offices")
@Tag(name = "Offices", description = "Gerenciamento de escritórios e filiais")
public class OfficesController {

    @Autowired
    private OfficesService officesService;

    @Operation(summary = "Listar todos os escritórios", description = "Retorna uma lista paginada de todos os escritórios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escritórios retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<Page<OfficesResponseDTO>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<OfficesResponseDTO> offices = officesService.findAll(pageable);
        return ResponseEntity.ok(offices);
    }

    @Operation(summary = "Buscar escritórios com filtros", description = "Retorna uma lista paginada de escritórios com filtros aplicados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escritórios retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<OfficesResponseDTO>> findWithFilters(
            @Parameter(description = "Nome do escritório") @RequestParam(required = false) String name,
            @Parameter(description = "Código do escritório") @RequestParam(required = false) String code,
            @Parameter(description = "Cidade") @RequestParam(required = false) String city,
            @Parameter(description = "Estado") @RequestParam(required = false) String state,
            @Parameter(description = "ID do país") @RequestParam(required = false) Integer countryId,
            @Parameter(description = "Email") @RequestParam(required = false) String email,
            @Parameter(description = "Telefone") @RequestParam(required = false) String phone,
            @Parameter(description = "É escritório principal") @RequestParam(required = false) Boolean isMainOffice,
            @Parameter(description = "Status ativo") @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        
        OfficesFilterDTO filters = new OfficesFilterDTO(
                name, code, city, state, countryId, email, phone, isMainOffice, isActive
        );
        
        Page<OfficesResponseDTO> offices = officesService.findWithFilters(filters, pageable);
        return ResponseEntity.ok(offices);
    }

    @Operation(summary = "Buscar escritório por ID", description = "Retorna um escritório específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escritório encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = OfficesResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Escritório não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OfficesResponseDTO> findById(@PathVariable Integer id) {
        OfficesResponseDTO office = officesService.findById(id);
        return ResponseEntity.ok(office);
    }

    @Operation(summary = "Listar escritórios ativos", description = "Retorna uma lista de escritórios ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escritórios ativos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/active")
    public ResponseEntity<List<OfficesResponseDTO>> findActive() {
        List<OfficesResponseDTO> offices = officesService.findActive();
        return ResponseEntity.ok(offices);
    }

    @Operation(summary = "Buscar escritório por código", description = "Retorna um escritório pelo código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escritório encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Escritório não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<OfficesResponseDTO> findByCode(@PathVariable String code) {
        OfficesResponseDTO office = officesService.findByCode(code);
        return ResponseEntity.ok(office);
    }

    @Operation(summary = "Buscar escritório por email", description = "Retorna um escritório pelo email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escritório encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Escritório não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<OfficesResponseDTO> findByEmail(@PathVariable String email) {
        OfficesResponseDTO office = officesService.findByEmail(email);
        return ResponseEntity.ok(office);
    }

    @Operation(summary = "Buscar escritório principal", description = "Retorna o escritório principal da empresa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escritório principal encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Escritório principal não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/main")
    public ResponseEntity<OfficesResponseDTO> findMainOffice() {
        OfficesResponseDTO office = officesService.findMainOffice();
        return ResponseEntity.ok(office);
    }

    @Operation(summary = "Buscar escritórios por país", description = "Retorna escritórios de um país específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escritórios retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<OfficesResponseDTO>> findByCountry(@PathVariable Integer countryId) {
        List<OfficesResponseDTO> offices = officesService.findByCountry(countryId);
        return ResponseEntity.ok(offices);
    }

    @Operation(summary = "Buscar escritórios por cidade", description = "Retorna escritórios de uma cidade específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escritórios retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/city/{city}")
    public ResponseEntity<List<OfficesResponseDTO>> findByCity(@PathVariable String city) {
        List<OfficesResponseDTO> offices = officesService.findByCity(city);
        return ResponseEntity.ok(offices);
    }

    @Operation(summary = "Buscar escritórios por nome", description = "Retorna escritórios que contenham o nome especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escritórios retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<List<OfficesResponseDTO>> findByNameContaining(@PathVariable String name) {
        List<OfficesResponseDTO> offices = officesService.findByNameContaining(name);
        return ResponseEntity.ok(offices);
    }

    @Operation(summary = "Buscar escritórios por nome (search)", description = "Busca flexível por nome usando Criteria API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escritórios retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<OfficesResponseDTO>> searchByName(@PathVariable String name) {
        List<OfficesResponseDTO> offices = officesService.searchByName(name);
        return ResponseEntity.ok(offices);
    }

    @Operation(summary = "Buscar escritórios por código (search)", description = "Busca flexível por código usando Criteria API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de escritórios retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search/code/{code}")
    public ResponseEntity<List<OfficesResponseDTO>> searchByCode(@PathVariable String code) {
        List<OfficesResponseDTO> offices = officesService.searchByCode(code);
        return ResponseEntity.ok(offices);
    }

    @Operation(summary = "Criar novo escritório", description = "Cria um novo escritório no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Escritório criado com sucesso",
                    content = @Content(schema = @Schema(implementation = OfficesResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Escritório já existe"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<OfficesResponseDTO> create(@Valid @RequestBody CreateOfficesDTO dto) {
        OfficesResponseDTO office = officesService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(office);
    }

    @Operation(summary = "Atualizar escritório", description = "Atualiza um escritório existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escritório atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = OfficesResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Escritório não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OfficesResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody UpdateOfficesDTO dto) {
        OfficesResponseDTO office = officesService.update(id, dto);
        return ResponseEntity.ok(office);
    }

    @Operation(summary = "Desativar escritório", description = "Desativa um escritório (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Escritório desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Escritório não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        officesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
