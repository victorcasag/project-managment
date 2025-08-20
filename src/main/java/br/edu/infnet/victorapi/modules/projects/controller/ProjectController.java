package br.edu.infnet.victorapi.modules.projects.controller;

import br.edu.infnet.victorapi.modules.projects.dto.CheckCodeResponseDTO;
import br.edu.infnet.victorapi.modules.projects.dto.CreateProjectDTO;
import br.edu.infnet.victorapi.modules.projects.dto.ProjectFilterDTO;
import br.edu.infnet.victorapi.modules.projects.dto.ProjectResponseDTO;
import br.edu.infnet.victorapi.modules.projects.dto.UpdateProjectDTO;
import br.edu.infnet.victorapi.modules.projects.service.ProjectService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Project", description = "APIs para gerenciamento de projetos")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    @Operation(summary = "Listar todos os projetos", description = "Retorna uma lista paginada de projetos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<Page<ProjectResponseDTO>> getAll(
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filtro por nome do projeto")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filtro por código do projeto")
            @RequestParam(required = false) String code,
            @Parameter(description = "Filtro por tipo de projeto")
            @RequestParam(required = false) Integer projectTypeId,
            @Parameter(description = "Filtro por status do projeto")
            @RequestParam(required = false) Integer projectStatusId,
            @Parameter(description = "Filtro por cliente/fornecedor")
            @RequestParam(required = false) Integer clientSupplierId,
            @Parameter(description = "Filtro por área")
            @RequestParam(required = false) Integer areaId,
            @Parameter(description = "Filtro por departamento")
            @RequestParam(required = false) Integer departmentId,
            @Parameter(description = "Filtro por escritório")
            @RequestParam(required = false) Integer officeId,
            @Parameter(description = "Filtro por setor")
            @RequestParam(required = false) Integer sectorId,
            @Parameter(description = "Filtro por país")
            @RequestParam(required = false) Integer countryId,
            @Parameter(description = "Filtro por usuário responsável")
            @RequestParam(required = false) Integer responsibleUserId,
            @Parameter(description = "Filtro por proposta")
            @RequestParam(required = false) Integer proposalId,
            @Parameter(description = "Filtro por projeto pai")
            @RequestParam(required = false) Integer parentProjectId,
            @Parameter(description = "Data de início inicial")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateFrom,
            @Parameter(description = "Data de início final")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDateTo,
            @Parameter(description = "Data de fim inicial")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateFrom,
            @Parameter(description = "Data de fim final")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDateTo,
            @Parameter(description = "Orçamento mínimo")
            @RequestParam(required = false) BigDecimal budgetFrom,
            @Parameter(description = "Orçamento máximo")
            @RequestParam(required = false) BigDecimal budgetTo,
            @Parameter(description = "Filtro por status ativo")
            @RequestParam(required = false) Boolean active) {

        Pageable pageable = PageRequest.of(page, size);
        
        ProjectFilterDTO filters = new ProjectFilterDTO(
                name, null, code,
                departmentId, projectTypeId, projectTypeId,
                sectorId, areaId, areaId,
                projectStatusId, projectStatusId,
                parentProjectId, parentProjectId,
                countryId, countryId,
                clientSupplierId, clientSupplierId,
                null, null,
                proposalId, proposalId,
                departmentId, officeId, sectorId,
                responsibleUserId,
                null, null, null, null, null,
                null, budgetFrom, budgetTo,
                null, null, null, null,
                startDateFrom, startDateTo, endDateFrom, endDateTo,
                budgetFrom, budgetTo, active
        );
        
        Page<ProjectResponseDTO> projects = projectService.findWithFiltersSimple(filters, pageable);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos os projetos", description = "Retorna uma lista completa de projetos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<ProjectResponseDTO>> getAllWithoutPagination() {
        List<ProjectResponseDTO> projects = projectService.findAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar projeto por ID", description = "Retorna um projeto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto encontrado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjectResponseDTO> getById(
            @Parameter(description = "ID do projeto", required = true)
            @PathVariable Integer id) {
        ProjectResponseDTO project = projectService.findById(id);
        return ResponseEntity.ok(project);
    }

    @PostMapping
    @Operation(summary = "Criar novo projeto", description = "Cria um novo projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Projeto já existe")
    })
    public ResponseEntity<ProjectResponseDTO> create(
            @Parameter(description = "Dados do projeto a ser criado", required = true)
            @Valid @RequestBody CreateProjectDTO projectDTO) {
        ProjectResponseDTO createdProject = projectService.create(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar projeto", description = "Atualiza um projeto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito com dados existentes")
    })
    public ResponseEntity<ProjectResponseDTO> update(
            @Parameter(description = "ID do projeto", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Dados atualizados do projeto", required = true)
            @Valid @RequestBody UpdateProjectDTO projectDTO) {
        ProjectResponseDTO updatedProject = projectService.update(id, projectDTO);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir projeto", description = "Desativa um projeto (exclusão lógica)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Projeto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do projeto", required = true)
            @PathVariable Integer id) {
        projectService.delete(id);
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
        CheckCodeResponseDTO response = projectService.checkCodeAvailability(code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-project-type/{projectTypeId}")
    @Operation(summary = "Listar projetos por tipo", description = "Retorna projetos de um tipo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<ProjectResponseDTO>> getByProjectType(
            @Parameter(description = "ID do tipo de projeto", required = true)
            @PathVariable Integer projectTypeId) {
        List<ProjectResponseDTO> projects = projectService.findByProjectType(projectTypeId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-project-status/{projectStatusId}")
    @Operation(summary = "Listar projetos por status", description = "Retorna projetos de um status específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<ProjectResponseDTO>> getByProjectStatus(
            @Parameter(description = "ID do status do projeto", required = true)
            @PathVariable Integer projectStatusId) {
        List<ProjectResponseDTO> projects = projectService.findByProjectStatus(projectStatusId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-responsible-user/{userId}")
    @Operation(summary = "Listar projetos por responsável", description = "Retorna projetos de um usuário responsável específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<ProjectResponseDTO>> getByResponsibleUser(
            @Parameter(description = "ID do usuário responsável", required = true)
            @PathVariable Integer userId) {
        List<ProjectResponseDTO> projects = projectService.findByResponsibleUser(userId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/by-parent-project/{parentId}")
    @Operation(summary = "Listar subprojetos", description = "Retorna subprojetos de um projeto pai específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<ProjectResponseDTO>> getByParentProject(
            @Parameter(description = "ID do projeto pai", required = true)
            @PathVariable Integer parentId) {
        List<ProjectResponseDTO> projects = projectService.findByParentProject(parentId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/total-budget")
    @Operation(summary = "Obter orçamento total", description = "Retorna o orçamento total de todos os projetos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orçamento total calculado com sucesso")
    })
    public ResponseEntity<BigDecimal> getTotalBudget() {
        BigDecimal totalBudget = projectService.getTotalBudget();
        return ResponseEntity.ok(totalBudget);
    }

    @GetMapping("/total-spent")
    @Operation(summary = "Obter valor total gasto", description = "Retorna o valor total gasto em todos os projetos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor total gasto calculado com sucesso")
    })
    public ResponseEntity<BigDecimal> getTotalSpentAmount() {
        BigDecimal totalSpent = projectService.getTotalSpentAmount();
        return ResponseEntity.ok(totalSpent);
    }
}