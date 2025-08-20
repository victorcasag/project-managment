package br.edu.infnet.victorapi.modules.proposalstatus.controller;

import br.edu.infnet.victorapi.modules.proposalstatus.entity.ProposalStatus;
import br.edu.infnet.victorapi.modules.proposalstatus.services.ProposalStatusService;
import br.edu.infnet.victorapi.modules.proposalstatus.dto.*;
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
@RequestMapping("/api/v1/proposal-statuses")
@Tag(name = "Proposal Statuses", description = "API para gerenciamento de status de propostas")
@SecurityRequirement(name = "bearerAuth")
public class ProposalStatusController {

    private final ProposalStatusService proposalStatusService;

    @Autowired
    public ProposalStatusController(ProposalStatusService proposalStatusService) {
        this.proposalStatusService = proposalStatusService;
    }

    @Operation(
            summary = "Criar novo status de proposta",
            description = "Cria um novo status de proposta no sistema. Apenas administradores podem criar status."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Status de proposta criado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProposalStatus.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "409", description = "Nome ou código já está em uso")
    })
    @PostMapping
    public ResponseEntity<?> createProposalStatus(@Valid @RequestBody CreateProposalStatusDTO createProposalStatusDTO) {
        try {
            if (!proposalStatusService.isValidProposalStatusName(createProposalStatusDTO.name())) {
                return ResponseEntity.badRequest().body("Nome do status de proposta é inválido");
            }

            if (createProposalStatusDTO.code() != null && !proposalStatusService.isValidProposalStatusCode(createProposalStatusDTO.code())) {
                return ResponseEntity.badRequest().body("Código do status de proposta é inválido");
            }

            ProposalStatus newProposalStatus = proposalStatusService.createProposalStatus(createProposalStatusDTO);
            ProposalStatusResponseDTO responseDTO = proposalStatusService.convertToResponseDTO(newProposalStatus);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar status de proposta por ID",
            description = "Retorna um status de proposta específico pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Status de proposta encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProposalStatusResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Status de proposta não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProposalStatusById(
            @Parameter(description = "ID do status de proposta", required = true)
            @PathVariable Integer id) {
        try {
            Optional<ProposalStatus> proposalStatus = proposalStatusService.getProposalStatusById(id);
            if (proposalStatus.isPresent()) {
                ProposalStatusResponseDTO responseDTO = proposalStatusService.convertToResponseDTO(proposalStatus.get());
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
            summary = "Buscar status de proposta por código",
            description = "Retorna um status de proposta específico pelo código"
    )
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getProposalStatusByCode(
            @Parameter(description = "Código do status de proposta", required = true)
            @PathVariable String code) {
        try {
            Optional<ProposalStatus> proposalStatus = proposalStatusService.getProposalStatusByCode(code);
            if (proposalStatus.isPresent()) {
                ProposalStatusResponseDTO responseDTO = proposalStatusService.convertToResponseDTO(proposalStatus.get());
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
            summary = "Listar todos os status de propostas",
            description = "Retorna uma lista paginada de todos os status de propostas ativos"
    )
    @GetMapping
    public ResponseEntity<?> getAllProposalStatuses(
            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo para ordenação", example = "sortOrder")
            @RequestParam(defaultValue = "sortOrder") String sortBy,

            @Parameter(description = "Direção da ordenação", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() :
                    Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<ProposalStatus> proposalStatuses = proposalStatusService.getAllProposalStatuses(pageable);

            Page<ProposalStatusResponseDTO> responsePage = proposalStatuses.map(proposalStatusService::convertToResponseDTO);
            return ResponseEntity.ok(responsePage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Atualizar status de proposta",
            description = "Atualiza informações de um status de proposta existente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProposalStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProposalStatusDTO updateProposalStatusDTO) {
        try {
            ProposalStatus updatedProposalStatus = proposalStatusService.updateProposalStatus(id, updateProposalStatusDTO);
            ProposalStatusResponseDTO responseDTO = proposalStatusService.convertToResponseDTO(updatedProposalStatus);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Desativar status de proposta",
            description = "Desativa um status de proposta (soft delete). Apenas administradores."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProposalStatus(@PathVariable Integer id) {
        try {
            proposalStatusService.deleteProposalStatus(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar status de propostas por nome",
            description = "Busca status de propostas cujo nome contenha o texto fornecido"
    )
    @GetMapping("/search")
    public ResponseEntity<?> searchProposalStatuses(
            @Parameter(description = "Nome para busca", required = true)
            @RequestParam String name) {
        try {
            List<ProposalStatus> proposalStatuses = proposalStatusService.searchProposalStatusesByName(name);
            List<ProposalStatusResponseDTO> responseDTOs = proposalStatusService.convertToResponseDTOList(proposalStatuses);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar status de propostas com filtros avançados",
            description = "Busca status de propostas com múltiplos filtros opcionais"
    )
    @GetMapping("/filter")
    public ResponseEntity<?> getProposalStatusesWithFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isInitial,
            @RequestParam(required = false) Boolean isFinal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("sortOrder").and(Sort.by("name")));
            Page<ProposalStatus> proposalStatuses = proposalStatusService.getProposalStatusesWithFilters(
                    name, code, isActive, isInitial, isFinal, pageable);
            Page<ProposalStatusResponseDTO> responsePage = proposalStatuses.map(proposalStatusService::convertToResponseDTO);
            return ResponseEntity.ok(responsePage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Ativar status de proposta",
            description = "Ativa um status de proposta desativado"
    )
    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateProposalStatus(@PathVariable Integer id) {
        try {
            boolean success = proposalStatusService.activateProposalStatus(id);
            if (success) {
                return ResponseEntity.ok("Status de proposta ativado com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Status de proposta não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Desativar status de proposta",
            description = "Desativa um status de proposta ativo"
    )
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateProposalStatus(@PathVariable Integer id) {
        try {
            boolean success = proposalStatusService.deactivateProposalStatus(id);
            if (success) {
                return ResponseEntity.ok("Status de proposta desativado com sucesso");
            } else {
                return ResponseEntity.badRequest().body("Status de proposta não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Verificar disponibilidade de código",
            description = "Verifica se um código de status de proposta está disponível"
    )
    @GetMapping("/check-code/{code}")
    public ResponseEntity<?> checkCodeAvailability(@PathVariable String code) {
        try {
            boolean exists = proposalStatusService.existsByCode(code);
            return ResponseEntity.ok(new CheckCodeResponseDTO(code, !exists,
                    exists ? "Código já está em uso" : "Código disponível"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Verificar disponibilidade de nome",
            description = "Verifica se um nome de status de proposta está disponível"
    )
    @GetMapping("/check-name/{name}")
    public ResponseEntity<?> checkNameAvailability(@PathVariable String name) {
        try {
            boolean exists = proposalStatusService.existsByName(name);
            return ResponseEntity.ok(new CheckNameResponseDTO(name, !exists,
                    exists ? "Nome já está em uso" : "Nome disponível"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Contar status de propostas ativos",
            description = "Retorna a quantidade total de status de propostas ativos"
    )
    @GetMapping("/stats/count")
    public ResponseEntity<?> countActiveProposalStatuses() {
        try {
            Long count = proposalStatusService.countActiveProposalStatuses();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter status de propostas recentes",
            description = "Retorna status de propostas criados nos últimos X dias"
    )
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentProposalStatuses(
            @Parameter(description = "Número de dias", example = "7")
            @RequestParam(defaultValue = "7") int days) {
        try {
            List<ProposalStatus> recentProposalStatuses = proposalStatusService.getRecentProposalStatuses(days);
            List<ProposalStatusResponseDTO> responseDTOs = proposalStatusService.convertToResponseDTOList(recentProposalStatuses);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Listar status de propostas ativos simples",
            description = "Retorna uma lista simples de todos os status de propostas ativos (sem paginação)"
    )
    @GetMapping("/active")
    public ResponseEntity<?> getActiveProposalStatuses() {
        try {
            List<ProposalStatus> proposalStatuses = proposalStatusService.getAllActiveProposalStatuses();
            List<ProposalStatusResponseDTO> responseDTOs = proposalStatusService.convertToResponseDTOList(proposalStatuses);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter status iniciais",
            description = "Retorna todos os status marcados como iniciais"
    )
    @GetMapping("/initial")
    public ResponseEntity<?> getInitialStatuses() {
        try {
            List<ProposalStatus> initialStatuses = proposalStatusService.getInitialStatuses();
            List<ProposalStatusResponseDTO> responseDTOs = proposalStatusService.convertToResponseDTOList(initialStatuses);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter status finais",
            description = "Retorna todos os status marcados como finais"
    )
    @GetMapping("/final")
    public ResponseEntity<?> getFinalStatuses() {
        try {
            List<ProposalStatus> finalStatuses = proposalStatusService.getFinalStatuses();
            List<ProposalStatusResponseDTO> responseDTOs = proposalStatusService.convertToResponseDTOList(finalStatuses);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}
