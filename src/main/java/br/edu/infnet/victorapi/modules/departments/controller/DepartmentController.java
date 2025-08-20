package br.edu.infnet.victorapi.modules.departments.controller;

import br.edu.infnet.victorapi.modules.departments.dto.DepartmentResponseDTO;
import br.edu.infnet.victorapi.modules.departments.dto.CreateDepartmentDTO;
import br.edu.infnet.victorapi.modules.departments.dto.DepartmentFilterDTO;
import br.edu.infnet.victorapi.modules.departments.dto.UpdateDepartmentDTO;
import br.edu.infnet.victorapi.modules.departments.services.DepartmentService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Departments", description = "Gerenciamento de departamentos")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Operation(summary = "Listar todos os departamentos", description = "Retorna uma lista paginada de todos os departamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de departamentos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<Page<DepartmentResponseDTO>> findAll(
            @Parameter(description = "Número da página", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Campo para ordenação", example = "name") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        
        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase("desc") ?
                org.springframework.data.domain.Sort.by(sortBy).descending() :
                org.springframework.data.domain.Sort.by(sortBy).ascending();
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        Page<DepartmentResponseDTO> departments = departmentService.findAll(pageable);
        return ResponseEntity.ok(departments);
    }

    @Operation(summary = "Buscar departamentos com filtros", description = "Retorna uma lista paginada de departamentos com filtros aplicados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de departamentos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<DepartmentResponseDTO>> findWithFilters(
            @Parameter(description = "Nome do departamento") @RequestParam(required = false) String name,
            @Parameter(description = "Código do departamento") @RequestParam(required = false) String code,
            @Parameter(description = "Descrição do departamento") @RequestParam(required = false) String description,
            @Parameter(description = "Status ativo") @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Número da página", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página", example = "20") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Campo para ordenação", example = "name") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Direção da ordenação", example = "asc") @RequestParam(defaultValue = "asc") String sortDir) {
        
        DepartmentFilterDTO filters = new DepartmentFilterDTO(
                name, code, description, isActive
        );
        
        org.springframework.data.domain.Sort sort = sortDir.equalsIgnoreCase("desc") ?
                org.springframework.data.domain.Sort.by(sortBy).descending() :
                org.springframework.data.domain.Sort.by(sortBy).ascending();
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sort);
        Page<DepartmentResponseDTO> departments = departmentService.findWithFilters(filters, pageable);
        return ResponseEntity.ok(departments);
    }

    @Operation(summary = "Buscar departamento por ID", description = "Retorna um departamento específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departamento encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = DepartmentResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> findById(@PathVariable Integer id) {
        DepartmentResponseDTO department = departmentService.findById(id);
        return ResponseEntity.ok(department);
    }

    @Operation(summary = "Listar departamentos ativos", description = "Retorna uma lista de departamentos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de departamentos ativos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/active")
    public ResponseEntity<List<DepartmentResponseDTO>> findActive() {
        List<DepartmentResponseDTO> departments = departmentService.findActive();
        return ResponseEntity.ok(departments);
    }

    @Operation(summary = "Buscar departamento por código", description = "Retorna um departamento pelo código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departamento encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<DepartmentResponseDTO> findByCode(@PathVariable String code) {
        DepartmentResponseDTO department = departmentService.findByCode(code);
        return ResponseEntity.ok(department);
    }

    @Operation(summary = "Criar novo departamento", description = "Cria um novo departamento no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Departamento criado com sucesso",
                    content = @Content(schema = @Schema(implementation = DepartmentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Departamento já existe"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> create(@Valid @RequestBody CreateDepartmentDTO dto) {
        DepartmentResponseDTO department = departmentService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(department);
    }

    @Operation(summary = "Atualizar departamento", description = "Atualiza um departamento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Departamento atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = DepartmentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody UpdateDepartmentDTO dto) {
        DepartmentResponseDTO department = departmentService.update(id, dto);
        return ResponseEntity.ok(department);
    }

    @Operation(summary = "Desativar departamento", description = "Desativa um departamento (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Departamento desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Departamento não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
