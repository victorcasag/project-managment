package br.edu.infnet.victorapi.modules.area.controller;

import br.edu.infnet.victorapi.common.entity.Auditable;
import br.edu.infnet.victorapi.modules.area.entity.Area;
import br.edu.infnet.victorapi.modules.area.entity.SpecializedArea;
import br.edu.infnet.victorapi.modules.area.service.AreaPolymorphismService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/demo/polymorphism")
@Tag(name = "Polymorphism Demo", description = "Demonstração de Herança e Polimorfismo")
public class PolymorphismDemoController {

    @Autowired
    private AreaPolymorphismService polymorphismService;

    @Operation(
        summary = "Demonstrar polimorfismo com validação",
        description = "Cria diferentes tipos de Area e demonstra como a validação polimórfica funciona"
    )
    @GetMapping("/validation-demo")
    public ResponseEntity<String> demonstratePolymorphicValidation() {
        StringBuilder result = new StringBuilder();
        result.append("=== DEMONSTRAÇÃO DE POLIMORFISMO - VALIDAÇÃO ===\n\n");

        Area standardArea = new Area("Vendas", "VND", "Área de vendas padrão");
        SpecializedArea specializedArea = new SpecializedArea("TI Especializada", "TI_ESP", 
            "Área de TI com especialização", "SECURITY", 5);

        result.append("1. Área Padrão:\n");
        result.append("   Válida: ").append(polymorphismService.validateEntity(standardArea)).append("\n");
        result.append("   Tipo: ").append(standardArea.getClass().getSimpleName()).append("\n\n");

        result.append("2. Área Especializada:\n");
        result.append("   Válida: ").append(polymorphismService.validateEntity(specializedArea)).append("\n");
        result.append("   Tipo: ").append(specializedArea.getClass().getSimpleName()).append("\n\n");

        SpecializedArea invalidSpecialized = new SpecializedArea("", "", "", null, null);
        result.append("3. Área Especializada Inválida:\n");
        result.append("   Válida: ").append(polymorphismService.validateEntity(invalidSpecialized)).append("\n");
        result.append("   Mostra como cada classe tem suas próprias regras de validação\n");

        return ResponseEntity.ok(result.toString());
    }

    @Operation(
        summary = "Demonstrar auditoria polimórfica",
        description = "Gera relatório de auditoria usando interface Auditable com polimorfismo"
    )
    @GetMapping("/audit-demo")
    public ResponseEntity<String> demonstratePolymorphicAudit() {
        Area area1 = new Area("Marketing", "MKT", "Área de marketing");
        SpecializedArea area2 = new SpecializedArea("Segurança", "SEC", 
            "Área de segurança crítica", "SECURITY", 5);
        area2.setRequiresCertification(true);
        area2.setBudgetLimit(100000.0);

        List<Auditable> auditableEntities = Arrays.asList(area1, area2);

        String auditReport = polymorphismService.generateAuditReport(auditableEntities);

        return ResponseEntity.ok(auditReport);
    }

    @Operation(
        summary = "Demonstrar processamento polimórfico",
        description = "Processa diferentes tipos de Area mostrando comportamento específico"
    )
    @PostMapping("/process-area")
    public ResponseEntity<String> processAreaPolymorphically(
        @Parameter(description = "Tipo de área: STANDARD ou SPECIALIZED")
        @RequestParam String type,
        @Parameter(description = "Nome da área")
        @RequestParam String name,
        @Parameter(description = "Código da área")
        @RequestParam String code,
        @Parameter(description = "Descrição da área")
        @RequestParam String description
    ) {
        try {
            Area area = polymorphismService.createArea(type, name, code, description);
            String result = polymorphismService.processArea(area);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Demonstrar herança multinível",
        description = "Mostra a hierarquia: BaseEntity -> Area -> SpecializedArea"
    )
    @GetMapping("/inheritance-hierarchy")
    public ResponseEntity<String> demonstrateInheritanceHierarchy() {
        StringBuilder result = new StringBuilder();
        result.append("=== DEMONSTRAÇÃO DE HERANÇA MULTINÍVEL ===\n\n");

        SpecializedArea specialized = new SpecializedArea("Laboratório", "LAB", 
            "Laboratório de pesquisa", "RESEARCH", 4);
        specialized.setBudgetLimit(50000.0);
        specialized.setRequiresCertification(true);

        result.append("Objeto: SpecializedArea\n");
        result.append("Hierarquia: BaseEntity -> Area -> SpecializedArea\n\n");

        result.append("1. De BaseEntity:\n");
        result.append("   ID: ").append(specialized.getId()).append("\n");
        result.append("   Ativo: ").append(specialized.getIsActive()).append("\n");
        result.append("   Válido: ").append(specialized.isValid()).append("\n\n");

        result.append("2. De Area:\n");
        result.append("   Nome: ").append(specialized.getName()).append("\n");
        result.append("   Código: ").append(specialized.getCode()).append("\n");
        result.append("   Descrição: ").append(specialized.getDescription()).append("\n\n");

        result.append("3. Específicos de SpecializedArea:\n");
        result.append("   Tipo de especialização: ").append(specialized.getSpecializationType()).append("\n");
        result.append("   Nível de prioridade: ").append(specialized.getPriorityLevel()).append("\n");
        result.append("   Requer certificação: ").append(specialized.getRequiresCertification()).append("\n");
        result.append("   Limite de orçamento: R$ ").append(specialized.getBudgetLimit()).append("\n");
        result.append("   Custo operacional: R$ ").append(String.format("%.2f", specialized.calculateOperationalCost())).append("\n");
        result.append("   Requer acesso alto nível: ").append(specialized.requiresHighLevelAccess()).append("\n\n");

        result.append("4. Polimorfismo (métodos sobrescritos):\n");
        result.append("   Audit Info (específico): ").append(specialized.getAuditInfo()).append("\n");
        result.append("   Audit Identifier (específico): ").append(specialized.getAuditIdentifier()).append("\n");

        return ResponseEntity.ok(result.toString());
    }

    @Operation(
        summary = "Demonstrar Template Method Pattern",
        description = "Mostra como métodos template permitem comportamentos específicos"
    )
    @PostMapping("/template-method-demo")
    public ResponseEntity<String> demonstrateTemplateMethod() {
        StringBuilder result = new StringBuilder();
        result.append("=== DEMONSTRAÇÃO DE TEMPLATE METHOD PATTERN ===\n\n");

        Area standardArea = new Area("Financeiro", "FIN", "Área financeira");
        SpecializedArea specializedArea = new SpecializedArea("Auditoria", "AUD", 
            "Auditoria especializada", "COMPLIANCE", 5);

        result.append("Ativando entidades usando Template Method Pattern:\n\n");

        result.append("1. Área Padrão:\n");
        result.append("   Antes: Ativa = ").append(standardArea.getIsActive()).append("\n");
        standardArea.activate();
        result.append("   Depois: Ativa = ").append(standardArea.getIsActive()).append("\n");
        result.append("   Processo: Validação -> Ativação -> Hook onActivate()\n\n");

        result.append("2. Área Especializada:\n");
        result.append("   Antes: Ativa = ").append(specializedArea.getIsActive()).append("\n");
        specializedArea.activate();
        result.append("   Depois: Ativa = ").append(specializedArea.getIsActive()).append("\n");
        result.append("   Processo: Validação específica -> Ativação -> Hook onActivate() específico\n\n");

        result.append("Template Method Pattern permite:\n");
        result.append("- Processo comum (activate/deactivate)\n");
        result.append("- Hooks específicos (onActivate/onDeactivate)\n");
        result.append("- Validações customizadas (canActivate/canDeactivate)\n");

        return ResponseEntity.ok(result.toString());
    }
}
