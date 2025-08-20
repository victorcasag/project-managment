package br.edu.infnet.victorapi.modules.projecttype.controller;

import br.edu.infnet.victorapi.modules.projecttype.dto.CheckCodeResponseDTO;
import br.edu.infnet.victorapi.modules.projecttype.dto.CreateProjectTypeDTO;
import br.edu.infnet.victorapi.modules.projecttype.dto.ProjectTypeResponseDTO;
import br.edu.infnet.victorapi.modules.projecttype.dto.UpdateProjectTypeDTO;
import br.edu.infnet.victorapi.modules.projecttype.service.ProjectTypeService;
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
@RequestMapping("/api/projecttypes")
@Tag(name = "ProjectType", description = "APIs para gerenciamento de tipos de projeto")
public class ProjectTypeController {

    @Autowired
    private ProjectTypeService projectTypeService;

    @GetMapping
    @Operation(summary = "Listar todos os tipos de projeto", description = "Retorna uma lista paginada de tipos de projeto ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<Page<ProjectTypeResponseDTO>> getAll(
            @Parameter(description = "Número da página (começando em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Filtro por nome do tipo de projeto")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filtro por código do tipo de projeto")
            @RequestParam(required = false) String code,
            @Parameter(description = "Filtro por status ativo")
            @RequestParam(required = false) Boolean active) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProjectTypeResponseDTO> projectTypes = projectTypeService.findWithFilters(name, code, active, pageable);
        return ResponseEntity.ok(projectTypes);
    }

    @GetMapping("/all")
    @Operation(summary = "Listar todos os tipos de projeto", description = "Retorna uma lista completa de tipos de projeto ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    public ResponseEntity<List<ProjectTypeResponseDTO>> getAllWithoutPagination() {
        List<ProjectTypeResponseDTO> projectTypes = projectTypeService.findAll();
        return ResponseEntity.ok(projectTypes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tipo de projeto por ID", description = "Retorna um tipo de projeto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de projeto encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo de projeto não encontrado")
    })
    public ResponseEntity<ProjectTypeResponseDTO> getById(
            @Parameter(description = "ID do tipo de projeto", required = true)
            @PathVariable Integer id) {
        ProjectTypeResponseDTO projectType = projectTypeService.findById(id);
        return ResponseEntity.ok(projectType);
    }

    @PostMapping
    @Operation(summary = "Criar novo tipo de projeto", description = "Cria um novo tipo de projeto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de projeto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Tipo de projeto já existe")
    })
    public ResponseEntity<ProjectTypeResponseDTO> create(
            @Parameter(description = "Dados do tipo de projeto a ser criado", required = true)
            @Valid @RequestBody CreateProjectTypeDTO projectTypeDTO) {
        ProjectTypeResponseDTO createdProjectType = projectTypeService.create(projectTypeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProjectType);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tipo de projeto", description = "Atualiza um tipo de projeto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de projeto atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Tipo de projeto não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito com dados existentes")
    })
    public ResponseEntity<ProjectTypeResponseDTO> update(
            @Parameter(description = "ID do tipo de projeto", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Dados atualizados do tipo de projeto", required = true)
            @Valid @RequestBody UpdateProjectTypeDTO projectTypeDTO) {
        ProjectTypeResponseDTO updatedProjectType = projectTypeService.update(id, projectTypeDTO);
        return ResponseEntity.ok(updatedProjectType);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tipo de projeto", description = "Desativa um tipo de projeto (exclusão lógica)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de projeto excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de projeto não encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do tipo de projeto", required = true)
            @PathVariable Integer id) {
        projectTypeService.delete(id);
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
        CheckCodeResponseDTO response = projectTypeService.checkCodeAvailability(code);
        return ResponseEntity.ok(response);
    }
}
