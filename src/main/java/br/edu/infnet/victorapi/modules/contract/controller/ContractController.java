package br.edu.infnet.victorapi.modules.contract.controller;

import br.edu.infnet.victorapi.modules.contract.dto.ContractFilterDTO;
import br.edu.infnet.victorapi.modules.contract.dto.ContractResponseDTO;
import br.edu.infnet.victorapi.modules.contract.dto.CreateContractDTO;
import br.edu.infnet.victorapi.modules.contract.dto.UpdateContractDTO;
import br.edu.infnet.victorapi.modules.contract.services.ContractService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contracts")
@Tag(name = "Contracts", description = "Gerenciamento de contratos")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Operation(summary = "Listar todos os contratos", description = "Retorna uma lista paginada de todos os contratos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contratos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<Page<ContractResponseDTO>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<ContractResponseDTO> contracts = contractService.findAll(pageable);
        return ResponseEntity.ok(contracts);
    }

    @Operation(summary = "Buscar contratos com filtros", description = "Retorna uma lista paginada de contratos com filtros aplicados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contratos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ContractResponseDTO>> findWithFilters(
            @Parameter(description = "Nome do contrato") @RequestParam(required = false) String name,
            @Parameter(description = "Número do contrato") @RequestParam(required = false) String contractNumber,
            @Parameter(description = "Descrição do contrato") @RequestParam(required = false) String description,
            @Parameter(description = "ID do cliente/fornecedor") @RequestParam(required = false) Integer clientSupplierId,
            @Parameter(description = "ID do tipo de moeda") @RequestParam(required = false) Integer coinTypeId,
            @Parameter(description = "Data de início (início do período)") @RequestParam(required = false) LocalDate startDateFrom,
            @Parameter(description = "Data de início (fim do período)") @RequestParam(required = false) LocalDate startDateTo,
            @Parameter(description = "Data de fim (início do período)") @RequestParam(required = false) LocalDate endDateFrom,
            @Parameter(description = "Data de fim (fim do período)") @RequestParam(required = false) LocalDate endDateTo,
            @Parameter(description = "Valor mínimo") @RequestParam(required = false) BigDecimal valueFrom,
            @Parameter(description = "Valor máximo") @RequestParam(required = false) BigDecimal valueTo,
            @Parameter(description = "Status ativo") @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        
        ContractFilterDTO filters = new ContractFilterDTO(
                name, contractNumber, description, clientSupplierId, coinTypeId,
                startDateFrom, startDateTo, endDateFrom, endDateTo,
                valueFrom, valueTo, isActive
        );
        
        Page<ContractResponseDTO> contracts = contractService.findWithFilters(filters, pageable);
        return ResponseEntity.ok(contracts);
    }

    @Operation(summary = "Buscar contrato por ID", description = "Retorna um contrato específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ContractResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> findById(@PathVariable Integer id) {
        ContractResponseDTO contract = contractService.findById(id);
        return ResponseEntity.ok(contract);
    }

    @Operation(summary = "Listar contratos ativos", description = "Retorna uma lista de contratos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contratos ativos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/active")
    public ResponseEntity<List<ContractResponseDTO>> findActive() {
        List<ContractResponseDTO> contracts = contractService.findActive();
        return ResponseEntity.ok(contracts);
    }

    @Operation(summary = "Buscar contrato por número", description = "Retorna um contrato pelo número do contrato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/number/{contractNumber}")
    public ResponseEntity<ContractResponseDTO> findByContractNumber(@PathVariable String contractNumber) {
        ContractResponseDTO contract = contractService.findByContractNumber(contractNumber);
        return ResponseEntity.ok(contract);
    }

    @Operation(summary = "Buscar contratos por cliente/fornecedor", description = "Retorna contratos de um cliente/fornecedor específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contratos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/client-supplier/{clientSupplierId}")
    public ResponseEntity<List<ContractResponseDTO>> findByClientSupplier(@PathVariable Integer clientSupplierId) {
        List<ContractResponseDTO> contracts = contractService.findByClientSupplier(clientSupplierId);
        return ResponseEntity.ok(contracts);
    }

    @Operation(summary = "Buscar contratos por tipo de moeda", description = "Retorna contratos que usam um tipo de moeda específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de contratos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/coin-type/{coinTypeId}")
    public ResponseEntity<List<ContractResponseDTO>> findByCoinType(@PathVariable Integer coinTypeId) {
        List<ContractResponseDTO> contracts = contractService.findByCoinType(coinTypeId);
        return ResponseEntity.ok(contracts);
    }

    @Operation(summary = "Obter valor total dos contratos ativos", description = "Retorna o valor total de todos os contratos ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Valor total retornado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/total-value")
    public ResponseEntity<BigDecimal> getTotalActiveValue() {
        BigDecimal totalValue = contractService.getTotalActiveValue();
        return ResponseEntity.ok(totalValue);
    }

    @Operation(summary = "Criar novo contrato", description = "Cria um novo contrato no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contrato criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ContractResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Contrato já existe"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<ContractResponseDTO> create(@Valid @RequestBody CreateContractDTO dto) {
        ContractResponseDTO contract = contractService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(contract);
    }

    @Operation(summary = "Atualizar contrato", description = "Atualiza um contrato existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contrato atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ContractResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContractResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody UpdateContractDTO dto) {
        ContractResponseDTO contract = contractService.update(id, dto);
        return ResponseEntity.ok(contract);
    }

    @Operation(summary = "Desativar contrato", description = "Desativa um contrato (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contrato desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        contractService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
