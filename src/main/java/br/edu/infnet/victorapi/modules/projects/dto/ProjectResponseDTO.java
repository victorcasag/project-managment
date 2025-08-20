package br.edu.infnet.victorapi.modules.projects.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProjectResponseDTO(
        Integer id,
        String name,
        String description,
        Integer departmentsId,
        Integer projectTypesId,
        Integer sectorsId,
        Integer areasId,
        Integer projectStatusesId,
        Integer originProjectsId,
        Integer countriesId,
        Integer clientsSuppliersId,
        Integer lastProjectStatusesId,
        Integer coinTypeId,
        Integer originProposalId,
        Boolean billableFl,
        Boolean internationalFl,
        String projectDir,
        String site,
        Boolean isDefault,
        BigDecimal exchangeRate,
        String openingEmail,
        String classification,
        Boolean investimentFl,
        Boolean productFl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
