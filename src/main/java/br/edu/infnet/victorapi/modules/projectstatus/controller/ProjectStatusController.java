package br.edu.infnet.victorapi.modules.projectstatus.controller;

import br.edu.infnet.victorapi.modules.projectstatus.dto.CheckCodeResponseDTO;
import br.edu.infnet.victorapi.modules.projectstatus.dto.CreateProjectStatusDTO;
import br.edu.infnet.victorapi.modules.projectstatus.dto.ProjectStatusResponseDTO;
import br.edu.infnet.victorapi.modules.projectstatus.dto.UpdateProjectStatusDTO;
import br.edu.infnet.victorapi.modules.projectstatus.service.ProjectStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projectstatuses")
@Tag(name = "ProjectStatus", description = "APIs para gerenciamento de status de projeto")
public class ProjectStatusController {

    @Autowired
    private ProjectStatusService projectStatusService;

    @GetMapping
    @Operation(summary = "Listar todos os status de projeto", description = "Retorna uma lista paginada de status de projeto ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<Page<ProjectStatusResponseDTO>> getAll(
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filtro por nome do status")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filtro por código do status")
            @RequestParam(required = false) String code,
            @Parameter(description = "Filtro por cor")
            @RequestParam(required = false) String color,
            @Parameter(description = "Filtro por status ativo")
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Filtro por status inicial")
            @RequestParam(required = false) Boolean isInitial,
            @Parameter(description = "Filtro por status final")
            @RequestParam(required = false) Boolean isFinal) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectStatusResponseDTO> projectStatuses = projectStatusService.findWithFilters(name, code, color, active, isInitial, isFinal, pageable);
        return ResponseEntity.ok(projectStatuses);
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos os status de projeto", description = "Retorna uma lista completa de status de projeto ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<ProjectStatusResponseDTO>> getAllWithoutPagination() {
        List<ProjectStatusResponseDTO> projectStatuses = projectStatusService.findAll();
        return ResponseEntity.ok(projectStatuses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar status de projeto por ID", description = "Retorna um status de projeto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status de projeto encontrado"),
            @ApiResponse(responseCode = "404", description = "Status de projeto não encontrado")
    })
    public ResponseEntity<ProjectStatusResponseDTO> getById(
            @Parameter(description = "ID do status de projeto", required = true)
            @PathVariable Integer id) {
        ProjectStatusResponseDTO projectStatus = projectStatusService.findById(id);
        return ResponseEntity.ok(projectStatus);
    }

    @PostMapping
    @Operation(summary = "Criar novo status de projeto", description = "Cria um novo status de projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Status de projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Status de projeto já existe")
    })
    public ResponseEntity<ProjectStatusResponseDTO> create(
            @Parameter(description = "Dados do status de projeto a ser criado", required = true)
            @Valid @RequestBody CreateProjectStatusDTO projectStatusDTO) {
        ProjectStatusResponseDTO createdProjectStatus = projectStatusService.create(projectStatusDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectStatus);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar status de projeto", description = "Atualiza um status de projeto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status de projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Status de projeto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito com dados existentes")
    })
    public ResponseEntity<ProjectStatusResponseDTO> update(
            @Parameter(description = "ID do status de projeto", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Dados atualizados do status de projeto", required = true)
            @Valid @RequestBody UpdateProjectStatusDTO projectStatusDTO) {
        ProjectStatusResponseDTO updatedProjectStatus = projectStatusService.update(id, projectStatusDTO);
        return ResponseEntity.ok(updatedProjectStatus);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir status de projeto", description = "Desativa um status de projeto (exclusão lógica)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Status de projeto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Status de projeto não encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do status de projeto", required = true)
            @PathVariable Integer id) {
        projectStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-code/{code}")
    @Operation(summary = "Verificar disponibilidade do código", description = "Verifica se um código está disponível para uso")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificação realizada com sucesso")
    })
    public ResponseEntity<CheckCodeResponseDTO> checkCodeAvailability(
            @Parameter(description = "Código a ser verificado", required = true)
            @PathVariable String code) {
        CheckCodeResponseDTO response = projectStatusService.checkCodeAvailability(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/initial")
    @Operation(summary = "Buscar status inicial", description = "Retorna o status configurado como inicial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status inicial encontrado"),
            @ApiResponse(responseCode = "404", description = "Status inicial não encontrado")
    })
    public ResponseEntity<ProjectStatusResponseDTO> getInitialStatus() {
        ProjectStatusResponseDTO projectStatus = projectStatusService.findInitialStatus();
        return ResponseEntity.ok(projectStatus);
    }

    @GetMapping("/final")
    @Operation(summary = "Listar status finais", description = "Retorna todos os status configurados como finais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<ProjectStatusResponseDTO>> getFinalStatuses() {
        List<ProjectStatusResponseDTO> projectStatuses = projectStatusService.findFinalStatuses();
        return ResponseEntity.ok(projectStatuses);
    }
}
