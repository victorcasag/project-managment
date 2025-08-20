package br.edu.infnet.victorapi.modules.proposals.controller;

import br.edu.infnet.victorapi.modules.proposals.entity.Proposals;
import br.edu.infnet.victorapi.modules.proposals.services.ProposalService;
import br.edu.infnet.victorapi.modules.proposals.dto.*;
import br.edu.infnet.victorapi.modules.projects.dto.ProjectResponseDTO;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/proposals")
@Tag(name = "Proposals", description = "API para gerenciamento de propostas")
@SecurityRequirement(name = "bearerAuth")
public class ProposalController {

    private final ProposalService proposalService;

    @Autowired
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @Operation(
            summary = "Criar nova proposta",
            description = "Cria uma nova proposta no sistema. Administradores e gerentes podem criar propostas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Proposta criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProposalResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "409", description = "Número da proposta já está em uso")
    })
    @PostMapping
    public ResponseEntity<?> createProposal(@Valid @RequestBody CreateProposalDTO createProposalDTO) {
        try {
            if (!proposalService.isValidProposalName(createProposalDTO.name())) {
                return ResponseEntity.badRequest().body("Nome da proposta é inválido");
            }

            if (createProposalDTO.proposalNumber() != null && 
                proposalService.existsByProposalNumber(createProposalDTO.proposalNumber())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Número da proposta já está em uso");
            }

            Proposals newProposal = proposalService.createProposal(createProposalDTO);
            ProposalResponseDTO responseDTO = proposalService.convertToResponseDTO(newProposal);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar proposta por ID",
            description = "Retorna uma proposta específica pelo ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Proposta encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProposalResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Proposta não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProposalById(
            @Parameter(description = "ID da proposta", required = true)
            @PathVariable Integer id) {
        try {
            Optional<Proposals> proposal = proposalService.getProposalById(id);
            if (proposal.isPresent()) {
                ProposalResponseDTO responseDTO = proposalService.convertToResponseDTO(proposal.get());
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
            summary = "Buscar proposta por número",
            description = "Retorna uma proposta específica pelo número"
    )
    @GetMapping("/number/{proposalNumber}")
    public ResponseEntity<?> getProposalByNumber(
            @Parameter(description = "Número da proposta", required = true)
            @PathVariable String proposalNumber) {
        try {
            Optional<Proposals> proposal = proposalService.getProposalByNumber(proposalNumber);
            if (proposal.isPresent()) {
                ProposalResponseDTO responseDTO = proposalService.convertToResponseDTO(proposal.get());
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
            summary = "Listar todas as propostas",
            description = "Retorna uma lista paginada de todas as propostas"
    )
    @GetMapping
    public ResponseEntity<?> getAllProposals(
            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamanho da página", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo para ordenação", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Direção da ordenação", example = "desc")
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() :
                    Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<Proposals> proposals = proposalService.getAllProposals(pageable);

            Page<ProposalResponseDTO> responsePage = proposals.map(proposalService::convertToResponseDTO);
            return ResponseEntity.ok(responsePage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Atualizar proposta",
            description = "Atualiza informações de uma proposta existente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProposal(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProposalDTO updateProposalDTO) {
        try {
            Proposals updatedProposal = proposalService.updateProposal(id, updateProposalDTO);
            ProposalResponseDTO responseDTO = proposalService.convertToResponseDTO(updatedProposal);
            return ResponseEntity.ok(responseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Deletar proposta",
            description = "Remove uma proposta do sistema. Apenas administradores."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProposal(@PathVariable Integer id) {
        try {
            proposalService.deleteProposal(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar propostas por nome",
            description = "Busca propostas cujo nome contenha o texto fornecido"
    )
    @GetMapping("/search")
    public ResponseEntity<?> searchProposals(
            @Parameter(description = "Nome para busca", required = true)
            @RequestParam String name) {
        try {
            List<Proposals> proposals = proposalService.searchProposalsByName(name);
            List<ProposalResponseDTO> responseDTOs = proposalService.convertToResponseDTOList(proposals);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Buscar propostas com filtros avançados",
            description = "Busca propostas com múltiplos filtros opcionais"
    )
    @GetMapping("/filter")
    public ResponseEntity<?> getProposalsWithFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String proposalNumber,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) Integer sectorId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer responsibleId,
            @RequestParam(required = false) BigDecimal minValue,
            @RequestParam(required = false) BigDecimal maxValue,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Integer priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            ProposalsFilterDTO filterDTO = new ProposalsFilterDTO(
                    name, proposalNumber, departmentId, sectorId, statusId, responsibleId,
                    minValue, maxValue, startDate, endDate, priority);
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Proposals> proposals = proposalService.getProposalsWithFilters(filterDTO, pageable);
            Page<ProposalResponseDTO> responsePage = proposals.map(proposalService::convertToResponseDTO);
            return ResponseEntity.ok(responsePage);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Verificar disponibilidade de número de proposta",
            description = "Verifica se um número de proposta está disponível"
    )
    @GetMapping("/check-number/{proposalNumber}")
    public ResponseEntity<?> checkProposalNumberAvailability(@PathVariable String proposalNumber) {
        try {
            boolean exists = proposalService.existsByProposalNumber(proposalNumber);
            return ResponseEntity.ok(Map.of(
                    "proposalNumber", proposalNumber,
                    "available", !exists,
                    "message", exists ? "Número já está em uso" : "Número disponível"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Contar todas as propostas",
            description = "Retorna a quantidade total de propostas"
    )
    @GetMapping("/stats/count")
    public ResponseEntity<?> countAllProposals() {
        try {
            Long count = proposalService.countAllProposals();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter valor total das propostas",
            description = "Retorna o valor total de todas as propostas"
    )
    @GetMapping("/stats/total-value")
    public ResponseEntity<?> getTotalValue() {
        try {
            BigDecimal totalValue = proposalService.getTotalValue();
            return ResponseEntity.ok(totalValue);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter propostas recentes",
            description = "Retorna propostas criadas nos últimos X dias"
    )
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentProposals(
            @Parameter(description = "Número de dias", example = "7")
            @RequestParam(defaultValue = "7") int days) {
        try {
            List<Proposals> recentProposals = proposalService.getRecentProposals(days);
            List<ProposalResponseDTO> responseDTOs = proposalService.convertToResponseDTOList(recentProposals);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Obter propostas por responsável",
            description = "Retorna todas as propostas de um responsável específico"
    )
    @GetMapping("/responsible/{responsibleId}")
    public ResponseEntity<?> getProposalsByResponsible(
            @Parameter(description = "ID do responsável", required = true)
            @PathVariable Integer responsibleId) {
        try {
            List<Proposals> proposals = proposalService.getProposalsByResponsible(responsibleId);
            List<ProposalResponseDTO> responseDTOs = proposalService.convertToResponseDTOList(proposals);
            return ResponseEntity.ok(responseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Converter proposta em projeto",
            description = "Converte uma proposta aprovada em um novo projeto. Apenas administradores e gerentes."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Projeto criado com sucesso a partir da proposta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "404", description = "Proposta não encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflito - proposta já foi convertida")
    })
    @PostMapping("/{id}/convert-to-project")
    public ResponseEntity<?> convertProposalToProject(
            @Parameter(description = "ID da proposta", required = true, example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody ConvertProposalToProjectDTO convertDTO) {
        try {
            ProjectResponseDTO projectResponse = proposalService.convertProposalToProject(id, convertDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(projectResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}
