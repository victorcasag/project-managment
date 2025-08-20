package br.edu.infnet.victorapi.modules.sector.controller;

import br.edu.infnet.victorapi.modules.sector.entity.Sector;
import br.edu.infnet.victorapi.modules.sector.services.SectorService;
import br.edu.infnet.victorapi.modules.sector.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sectors")
@Tag(name = "Sectors", description = "API para gerenciamento de setores")
@SecurityRequirement(name = "bearerAuth")
public class SectorController {

    private final SectorService sectorService;

    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @Operation(
            summary = "Criar novo setor",
            description = "Cria um novo setor no sistema. Apenas administradores podem criar setores."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Setor criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Sector.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "409", description = "Nome ou código já está em uso")
    })
    @PostMapping
    public ResponseEntity<?> createSector(@Valid @RequestBody CreateSectorDTO createSectorDTO) {
        try {
            if (!sectorService.isValidSectorName(createSectorDTO.name())) {
                return ResponseEntity.badRequest().body("Nome do setor é inválido");
            }

            if (createSectorDTO.code() != null && !sectorService.isValidSectorCode(createSectorDTO.code())) {
                return ResponseEntity.badRequest().body("Código do setor é inválido");
            }

            Sector newSector = sectorService.createSector(createSectorDTO);
            SectorResponseDTO responseDTO = sectorService.convertToResponseDTO(newSector);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar setor por ID",
            description = "Retorna um setor específico pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Setor encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SectorResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Setor não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getSectorById(
            @Parameter(description = "ID do setor", required = true)
            @PathVariable Integer id) {
        try {
            Optional<Sector> sector = sectorService.getSectorById(id);
            if (sector.isPresent()) {
                SectorResponseDTO responseDTO = sectorService.convertToResponseDTO(sector.get());
                return ResponseEntity.ok(responseDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar setor por código",
            description = "Retorna um setor específico pelo código"
    )
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getSectorByCode(
            @Parameter(description = "Código do setor", required = true)
            @PathVariable String code) {
        try {
            Optional<Sector> sector = sectorService.getSectorByCode(code);
            if (sector.isPresent()) {
                SectorResponseDTO responseDTO = sectorService.convertToResponseDTO(sector.get());
                return ResponseEntity.ok(responseDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Listar todos os setores",
            description = "Retorna uma lista paginada de todos os setores ativos"
    )
    @GetMapping
    public ResponseEntity<?> getAllSectors(
            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo para ordenação", example = "name")
            @RequestParam(defaultValue = "name") String sortBy,

            @Parameter(description = "Direção da ordenação", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() :
                    Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Sector> sectors = sectorService.getAllSectors(pageable);

            Page<SectorResponseDTO> responsePage = sectors.map(sectorService::convertToResponseDTO);
            return ResponseEntity.ok(responsePage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Atualizar setor",
            description = "Atualiza informações de um setor existente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSector(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateSectorDTO updateSectorDTO) {
        try {
            Sector updatedSector = sectorService.updateSector(id, updateSectorDTO);
            SectorResponseDTO responseDTO = sectorService.convertToResponseDTO(updatedSector);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Desativar setor",
            description = "Desativa um setor (soft delete). Apenas administradores."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSector(@PathVariable Integer id) {
        try {
            sectorService.deleteSector(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar setores por nome",
            description = "Busca setores cujo nome contenha o texto fornecido"
    )
    @GetMapping("/search")
    public ResponseEntity<?> searchSectors(
            @Parameter(description = "Nome para busca", required = true)
            @RequestParam String name) {
        try {
            List<Sector> sectors = sectorService.searchSectorsByName(name);
            List<SectorResponseDTO> responseDTOs = sectorService.convertToResponseDTOList(sectors);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar setores com filtros avançados",
            description = "Busca setores com múltiplos filtros opcionais"
    )
    @GetMapping("/filter")
    public ResponseEntity<?> getSectorsWithFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
            Page<Sector> sectors = sectorService.getSectorsWithFilters(name, code, isActive, pageable);
            Page<SectorResponseDTO> responsePage = sectors.map(sectorService::convertToResponseDTO);
            return ResponseEntity.ok(responsePage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Ativar setor",
            description = "Ativa um setor desativado"
    )
    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateSector(@PathVariable Integer id) {
        try {
            boolean success = sectorService.activateSector(id);
            if (success) {
                return ResponseEntity.ok("Setor ativado com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Setor não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Desativar setor",
            description = "Desativa um setor ativo"
    )
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateSector(@PathVariable Integer id) {
        try {
            boolean success = sectorService.deactivateSector(id);
            if (success) {
                return ResponseEntity.ok("Setor desativado com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Setor não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Verificar disponibilidade de código",
            description = "Verifica se um código de setor está disponível"
    )
    @GetMapping("/check-code/{code}")
    public ResponseEntity<?> checkCodeAvailability(@PathVariable String code) {
        try {
            boolean exists = sectorService.existsByCode(code);
            return ResponseEntity.ok(new CheckCodeResponseDTO(code, !exists,
                    exists ? "Código já está em uso" : "Código disponível"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Verificar disponibilidade de nome",
            description = "Verifica se um nome de setor está disponível"
    )
    @GetMapping("/check-name/{name}")
    public ResponseEntity<?> checkNameAvailability(@PathVariable String name) {
        try {
            boolean exists = sectorService.existsByName(name);
            return ResponseEntity.ok(new CheckNameResponseDTO(name, !exists,
                    exists ? "Nome já está em uso" : "Nome disponível"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Contar setores ativos",
            description = "Retorna a quantidade total de setores ativos"
    )
    @GetMapping("/stats/count")
    public ResponseEntity<?> countActiveSectors() {
        try {
            Long count = sectorService.countActiveSectors();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter setores recentes",
            description = "Retorna setores criados nos últimos X dias"
    )
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentSectors(
            @Parameter(description = "Número de dias", example = "7")
            @RequestParam(defaultValue = "7") int days) {
        try {
            List<Sector> recentSectors = sectorService.getRecentSectors(days);
            List<SectorResponseDTO> responseDTOs = sectorService.convertToResponseDTOList(recentSectors);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Listar setores ativos simples",
            description = "Retorna uma lista simples de todos os setores ativos (sem paginação)"
    )
    @GetMapping("/active")
    public ResponseEntity<?> getActiveSectors() {
        try {
            List<Sector> sectors = sectorService.getAllActiveSectors();
            List<SectorResponseDTO> responseDTOs = sectorService.convertToResponseDTOList(sectors);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}