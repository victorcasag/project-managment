package br.edu.infnet.victorapi.modules.area.controller;

import br.edu.infnet.victorapi.modules.area.dto.AreaFilterDTO;
import br.edu.infnet.victorapi.modules.area.dto.AreaResponseDTO;
import br.edu.infnet.victorapi.modules.area.dto.CreateAreaDTO;
import br.edu.infnet.victorapi.modules.area.dto.UpdateAreaDTO;
import br.edu.infnet.victorapi.modules.area.services.AreaService;
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
@RequestMapping("/api/areas")
@Tag(name = "Areas", description = "Gerenciamento de áreas")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @Operation(summary = "Listar todas as áreas", description = "Retorna uma lista paginada de todas as áreas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de áreas retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<Page<AreaResponseDTO>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<AreaResponseDTO> areas = areaService.findAll(pageable);
        return ResponseEntity.ok(areas);
    }

    @Operation(summary = "Buscar áreas com filtros", description = "Retorna uma lista paginada de áreas com filtros aplicados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de áreas retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<AreaResponseDTO>> findWithFilters(
            @Parameter(description = "Nome da área") @RequestParam(required = false) String name,
            @Parameter(description = "Código da área") @RequestParam(required = false) String code,
            @Parameter(description = "Descrição da área") @RequestParam(required = false) String description,
            @Parameter(description = "Status ativo") @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        
        AreaFilterDTO filterDTO = new AreaFilterDTO(name, code, description, isActive);
        Page<AreaResponseDTO> areas = areaService.findWithFilters(filterDTO, pageable);
        return ResponseEntity.ok(areas);
    }

    @Operation(summary = "Buscar área por ID", description = "Retorna uma área específica pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área encontrada com sucesso",
                    content = @Content(schema = @Schema(implementation = AreaResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Área não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AreaResponseDTO> findById(@PathVariable Integer id) {
        AreaResponseDTO area = areaService.findById(id);
        return ResponseEntity.ok(area);
    }

    @Operation(summary = "Listar áreas ativas", description = "Retorna uma lista de áreas ativas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de áreas ativas retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/active")
    public ResponseEntity<List<AreaResponseDTO>> findActive() {
        List<AreaResponseDTO> areas = areaService.findActive();
        return ResponseEntity.ok(areas);
    }

    @Operation(summary = "Buscar área por código", description = "Retorna uma área pelo código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Área não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<AreaResponseDTO> findByCode(@PathVariable String code) {
        AreaResponseDTO area = areaService.findByCode(code);
        return ResponseEntity.ok(area);
    }

    @Operation(summary = "Buscar áreas por nome", description = "Retorna áreas que contenham o nome especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de áreas retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<List<AreaResponseDTO>> findByNameContaining(@PathVariable String name) {
        List<AreaResponseDTO> areas = areaService.findByNameContaining(name);
        return ResponseEntity.ok(areas);
    }

    @Operation(summary = "Buscar áreas por nome (search)", description = "Busca flexível por nome usando Criteria API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de áreas retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<AreaResponseDTO>> searchByName(@PathVariable String name) {
        List<AreaResponseDTO> areas = areaService.searchByName(name);
        return ResponseEntity.ok(areas);
    }

    @Operation(summary = "Buscar áreas por código (search)", description = "Busca flexível por código usando Criteria API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de áreas retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search/code/{code}")
    public ResponseEntity<List<AreaResponseDTO>> searchByCode(@PathVariable String code) {
        List<AreaResponseDTO> areas = areaService.searchByCode(code);
        return ResponseEntity.ok(areas);
    }

    @Operation(summary = "Criar nova área", description = "Cria uma nova área no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Área criada com sucesso",
                    content = @Content(schema = @Schema(implementation = AreaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Área já existe"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<AreaResponseDTO> create(@Valid @RequestBody CreateAreaDTO dto) {
        AreaResponseDTO area = areaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(area);
    }

    @Operation(summary = "Atualizar área", description = "Atualiza uma área existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Área atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = AreaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Área não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AreaResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody UpdateAreaDTO dto) {
        AreaResponseDTO area = areaService.update(id, dto);
        return ResponseEntity.ok(area);
    }

    @Operation(summary = "Desativar área", description = "Desativa uma área (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Área desativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Área não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        areaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
