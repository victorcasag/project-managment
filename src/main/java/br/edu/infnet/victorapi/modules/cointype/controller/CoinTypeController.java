package br.edu.infnet.victorapi.modules.cointype.controller;

import br.edu.infnet.victorapi.modules.cointype.dto.CoinTypeFilterDTO;
import br.edu.infnet.victorapi.modules.cointype.dto.CoinTypeResponseDTO;
import br.edu.infnet.victorapi.modules.cointype.dto.CreateCoinTypeDTO;
import br.edu.infnet.victorapi.modules.cointype.dto.UpdateCoinTypeDTO;
import br.edu.infnet.victorapi.modules.cointype.services.CoinTypeService;
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
@RequestMapping("/api/coin-types")
@Tag(name = "Coin Types", description = "Gerenciamento de tipos de moeda")
public class CoinTypeController {

    @Autowired
    private CoinTypeService coinTypeService;

    @Operation(summary = "Listar todos os tipos de moeda", description = "Retorna uma lista paginada de todos os tipos de moeda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de moeda retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<Page<CoinTypeResponseDTO>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        Page<CoinTypeResponseDTO> coinTypes = coinTypeService.findAll(pageable);
        return ResponseEntity.ok(coinTypes);
    }

    @Operation(summary = "Buscar tipos de moeda com filtros", description = "Retorna uma lista paginada de tipos de moeda com filtros aplicados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de moeda retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<CoinTypeResponseDTO>> findWithFilters(
            @Parameter(description = "Nome do tipo de moeda") @RequestParam(required = false) String name,
            @Parameter(description = "Código da moeda") @RequestParam(required = false) String code,
            @Parameter(description = "Símbolo da moeda") @RequestParam(required = false) String symbol,
            @Parameter(description = "Status ativo") @RequestParam(required = false) Boolean isActive,
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        
        CoinTypeFilterDTO filters = new CoinTypeFilterDTO(
                name, code, symbol, isActive
        );
        
        Page<CoinTypeResponseDTO> coinTypes = coinTypeService.findWithFilters(filters, pageable);
        return ResponseEntity.ok(coinTypes);
    }

    @Operation(summary = "Buscar tipo de moeda por ID", description = "Retorna um tipo de moeda específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de moeda encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = CoinTypeResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Tipo de moeda não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CoinTypeResponseDTO> findById(@PathVariable Integer id) {
        CoinTypeResponseDTO coinType = coinTypeService.findById(id);
        return ResponseEntity.ok(coinType);
    }

    @Operation(summary = "Listar tipos de moeda ativos", description = "Retorna uma lista de tipos de moeda ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de moeda ativos retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/active")
    public ResponseEntity<List<CoinTypeResponseDTO>> findActive() {
        List<CoinTypeResponseDTO> coinTypes = coinTypeService.findActive();
        return ResponseEntity.ok(coinTypes);
    }

    @Operation(summary = "Buscar tipo de moeda por código", description = "Retorna um tipo de moeda pelo código")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de moeda encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de moeda não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/code/{code}")
    public ResponseEntity<CoinTypeResponseDTO> findByCode(@PathVariable String code) {
        CoinTypeResponseDTO coinType = coinTypeService.findByCode(code);
        return ResponseEntity.ok(coinType);
    }

    @Operation(summary = "Buscar tipos de moeda por símbolo", description = "Retorna tipos de moeda que usam um símbolo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de moeda retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<List<CoinTypeResponseDTO>> findBySymbol(@PathVariable String symbol) {
        List<CoinTypeResponseDTO> coinTypes = coinTypeService.findBySymbol(symbol);
        return ResponseEntity.ok(coinTypes);
    }

    @Operation(summary = "Criar novo tipo de moeda", description = "Cria um novo tipo de moeda no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de moeda criado com sucesso",
                    content = @Content(schema = @Schema(implementation = CoinTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Tipo de moeda já existe"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<CoinTypeResponseDTO> create(@Valid @RequestBody CreateCoinTypeDTO dto) {
        CoinTypeResponseDTO coinType = coinTypeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(coinType);
    }

    @Operation(summary = "Atualizar tipo de moeda", description = "Atualiza um tipo de moeda existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tipo de moeda atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = CoinTypeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Tipo de moeda não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CoinTypeResponseDTO> update(@PathVariable Integer id, @Valid @RequestBody UpdateCoinTypeDTO dto) {
        CoinTypeResponseDTO coinType = coinTypeService.update(id, dto);
        return ResponseEntity.ok(coinType);
    }

    @Operation(summary = "Desativar tipo de moeda", description = "Desativa um tipo de moeda (soft delete)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tipo de moeda desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tipo de moeda não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        coinTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
