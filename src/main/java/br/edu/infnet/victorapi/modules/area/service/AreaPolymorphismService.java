package br.edu.infnet.victorapi.modules.area.service;

import br.edu.infnet.victorapi.common.entity.BaseEntity;
import br.edu.infnet.victorapi.common.entity.Auditable;
import br.edu.infnet.victorapi.modules.area.entity.Area;
import br.edu.infnet.victorapi.modules.area.entity.SpecializedArea;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class AreaPolymorphismService {

    public boolean validateEntity(BaseEntity entity) {
        return entity.isValid();
    }

    public String generateAuditReport(List<Auditable> auditableEntities) {
        StringBuilder report = new StringBuilder();
        report.append("=== RELATÓRIO DE AUDITORIA ===\n");
        
        for (Auditable entity : auditableEntities) {
            report.append("Entidade: ").append(entity.getAuditIdentifier()).append("\n");
            report.append("Info: ").append(entity.getAuditInfo()).append("\n");
            report.append("Modificada Recentemente: ").append(entity.isRecentlyModified() ? "SIM" : "NÃO").append("\n");
            report.append("---\n");
        }
        
        return report.toString();
    }

    public String processArea(Area area) {
        StringBuilder result = new StringBuilder();
        
        result.append("Processando área: ").append(area.getName()).append("\n");
        result.append("Válida: ").append(area.isValid() ? "SIM" : "NÃO").append("\n");
        
        if (area instanceof SpecializedArea specializedArea) {
            result.append("Tipo de especialização: ").append(specializedArea.getSpecializationType()).append("\n");
            result.append("Nível de prioridade: ").append(specializedArea.getPriorityLevel()).append("\n");
            result.append("Requer acesso alto nível: ").append(specializedArea.requiresHighLevelAccess() ? "SIM" : "NÃO").append("\n");
            result.append("Custo operacional: R$ ").append(String.format("%.2f", specializedArea.calculateOperationalCost())).append("\n");
        }
        
        return result.toString();
    }

    public void activateEntities(List<BaseEntity> entities) {
        for (BaseEntity entity : entities) {
            System.out.println("Iniciando ativação de: " + entity.getClass().getSimpleName());
            entity.activate();
            System.out.println("Entidade ativada: " + entity.toString());
        }
    }

    public List<String> getEntityDescriptions(List<Area> areas) {
        List<String> descriptions = new ArrayList<>();
        
        for (Area area : areas) {
            String description = area.getAuditInfo();
            
            if (area instanceof SpecializedArea specialized) {
                description += " [ESPECIALIZADA: " + specialized.getSpecializationType() + "]";
            }
            
            descriptions.add(description);
        }
        
        return descriptions;
    }

    public <T extends BaseEntity & Auditable> void processAuditableEntity(T entity) {
        if (entity.isValid()) {
            entity.activate();
        }
        
        System.out.println("Processando: " + entity.getAuditIdentifier());
        System.out.println("Info de auditoria: " + entity.getAuditInfo());
    }

    public Area createArea(String type, String name, String code, String description) {
        return switch (type.toUpperCase()) {
            case "SPECIALIZED" -> {
                SpecializedArea specialized = new SpecializedArea(name, code, description, "GENERAL", 3);
                specialized.setRequiresCertification(true);
                yield specialized;
            }
            case "STANDARD" -> new Area(name, code, description);
            default -> throw new IllegalArgumentException("Tipo de área não suportado: " + type);
        };
    }
}
