package br.edu.infnet.victorapi.modules.clientsupplier.controller;

import br.edu.infnet.victorapi.modules.clientsupplier.dto.ClientSupplierFilterDTO;
import br.edu.infnet.victorapi.modules.clientsupplier.dto.ClientSupplierResponseDTO;
import br.edu.infnet.victorapi.modules.clientsupplier.dto.CreateClientSupplierDTO;
import br.edu.infnet.victorapi.modules.clientsupplier.dto.UpdateClientSupplierDTO;
import br.edu.infnet.victorapi.modules.clientsupplier.services.ClientSupplierService;
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
@RequestMapping("/api/client-suppliers")
@Tag(name = "Client Suppliers", description = "Gerenciamento de clientes e fornecedores")
public class ClientSupplierController {

    @Autowired
    private ClientSupplierService clientSupplierService;

    @Operation(summary = "Listar todos os clientes/fornecedores", description = "Retorna uma lista paginada de todos os clientes/fornecedores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes/fornecedores retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<Page<ClientSupplierResponseDTO>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<ClientSupplierResponseDTO> clientSuppliers = clientSupplierService.findAll(pageable);
        return ResponseEntity.ok(clientSuppliers);
    }

    @Operation(summary = "Buscar clientes/fornecedores com filtros", description = "Retorna uma lista paginada de clientes/fornecedores com filtros aplicados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes/fornecedores retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ClientSupplierResponseDTO>> findWithFilters(
            @Parameter(description = "Nome do cliente/fornecedor") @RequestParam(required = false) String name,
            @Parameter(description = "Documento") @RequestParam(required = false) String document,
            @Parameter(description = "Tipo de documento") @RequestParam(required = false) String documentType,
            @Parameter(description = "Email") @RequestParam(required = false) String email,
            @Parameter(description = "Telefone") @RequestParam(required = false) String phone,
            @Parameter(description = "Cidade") @RequestParam(required = false) String city,
            @Parameter(description = "Estado") @RequestParam(required = false) String state,
            @Parameter(description = "Tipo (CLIENT, SUPPLIER, BOTH)") @RequestParam(required = false) String type,
            @Parameter(description = "ID do país") @RequestParam(required = false) Integer countryId,
            @Parameter(description = "Status ativo") @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        
        ClientSupplierFilterDTO filters = new ClientSupplierFilterDTO(
                name, document, documentType, email, phone, city, state, type, countryId, isActive
        );
        
        Page<ClientSupplierResponseDTO> clientSuppliers = clientSupplierService.findWithFilters(filters, pageable);
        return ResponseEntity.ok(clientSuppliers);
    }

    @Operation(summary = "Buscar cliente/fornecedor por ID", description = "Retorna um cliente/fornecedor específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente/fornecedor encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClientSupplierResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente/fornecedor não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientSupplierResponseDTO> findById(@PathVariable Integer id) {
        ClientSupplierResponseDTO clientSupplier = clientSupplierService.findById(id);
        return ResponseEntity.ok(clientSupplier);
    }

    @Operation(summary = "Listar clientes/fornecedores ativos", description = "Retorna uma lista de clientes/fornecedores ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes/fornecedores ativos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/active")
    public ResponseEntity<List<ClientSupplierResponseDTO>> findActive() {
        List<ClientSupplierResponseDTO> clientSuppliers = clientSupplierService.findActive();
        return ResponseEntity.ok(clientSuppliers);
    }

    @Operation(summary = "Buscar cliente/fornecedor por documento", description = "Retorna um cliente/fornecedor pelo documento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente/fornecedor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente/fornecedor não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/document/{document}")
    public ResponseEntity<ClientSupplierResponseDTO> findByDocument(@PathVariable String document) {
        ClientSupplierResponseDTO clientSupplier = clientSupplierService.findByDocument(document);
        return ResponseEntity.ok(clientSupplier);
    }

    @Operation(summary = "Buscar cliente/fornecedor por email", description = "Retorna um cliente/fornecedor pelo email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente/fornecedor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente/fornecedor não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<ClientSupplierResponseDTO> findByEmail(@PathVariable String email) {
        ClientSupplierResponseDTO clientSupplier = clientSupplierService.findByEmail(email);
        return ResponseEntity.ok(clientSupplier);
    }

    @Operation(summary = "Buscar por tipo", description = "Retorna clientes/fornecedores de um tipo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ClientSupplierResponseDTO>> findByType(@PathVariable String type) {
        List<ClientSupplierResponseDTO> clientSuppliers = clientSupplierService.findByType(type);
        return ResponseEntity.ok(clientSuppliers);
    }

    @Operation(summary = "Buscar por país", description = "Retorna clientes/fornecedores de um país específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<ClientSupplierResponseDTO>> findByCountry(@PathVariable Integer countryId) {
        List<ClientSupplierResponseDTO> clientSuppliers = clientSupplierService.findByCountry(countryId);
        return ResponseEntity.ok(clientSuppliers);
    }

    @Operation(summary = "Buscar por cidade", description = "Retorna clientes/fornecedores de uma cidade específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/city/{city}")
    public ResponseEntity<List<ClientSupplierResponseDTO>> findByCity(@PathVariable String city) {
        List<ClientSupplierResponseDTO> clientSuppliers = clientSupplierService.findByCity(city);
        return ResponseEntity.ok(clientSuppliers);
    }

    @Operation(summary = "Buscar por nome", description = "Retorna clientes/fornecedores que contenham o nome especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<List<ClientSupplierResponseDTO>> searchByName(@PathVariable String name) {
        List<ClientSupplierResponseDTO> clientSuppliers = clientSupplierService.searchByName(name);
        return ResponseEntity.ok(clientSuppliers);
    }

    @Operation(summary = "Listar apenas clientes", description = "Retorna uma lista de apenas clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/clients")
    public ResponseEntity<List<ClientSupplierResponseDTO>> findClients() {
        List<ClientSupplierResponseDTO> clients = clientSupplierService.findClients();
        return ResponseEntity.ok(clients);
    }

    @Operation(summary = "Listar apenas fornecedores", description = "Retorna uma lista de apenas fornecedores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de fornecedores retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/suppliers")
    public ResponseEntity<List<ClientSupplierResponseDTO>> findSuppliers() {
        List<ClientSupplierResponseDTO> suppliers = clientSupplierService.findSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @Operation(summary = "Criar novo cliente/fornecedor", description = "Cria um novo cliente/fornecedor no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente/fornecedor criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClientSupplierResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Cliente/fornecedor já existe"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<ClientSupplierResponseDTO> create(@Valid @RequestBody CreateClientSupplierDTO dto) {
        ClientSupplierResponseDTO clientSupplier = clientSupplierService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientSupplier);
    }

    @Operation(summary = "Atualizar cliente/fornecedor", description = "Atualiza um cliente/fornecedor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente/fornecedor atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClientSupplierResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente/fornecedor não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientSupplierResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody UpdateClientSupplierDTO dto) {
        ClientSupplierResponseDTO clientSupplier = clientSupplierService.update(id, dto);
        return ResponseEntity.ok(clientSupplier);
    }

    @Operation(summary = "Desativar cliente/fornecedor", description = "Desativa um cliente/fornecedor (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente/fornecedor desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente/fornecedor não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clientSupplierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
