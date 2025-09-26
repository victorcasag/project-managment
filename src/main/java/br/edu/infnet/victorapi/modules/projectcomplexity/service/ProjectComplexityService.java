package br.edu.infnet.victorapi.modules.projectcomplexity.service;

import br.edu.infnet.victorapi.modules.projectcomplexity.model.ComplexityLevel;
import br.edu.infnet.victorapi.modules.projectcomplexity.model.ProjectComplexityRequest;
import org.springframework.stereotype.Service;

@Service
public class ProjectComplexityService {

    // Returns a computed ComplexityLevel based on simple heuristic
    public ComplexityLevel evaluate(ProjectComplexityRequest req) {
        if (req == null) throw new IllegalArgumentException("request cannot be null");
        if (req.getTeamSize() < 0 || req.getWeeksDuration() < 0 || req.getExternalIntegrationsCount() < 0)
            throw new IllegalArgumentException("numeric values must be non-negative");

        int score = 0;

        // smaller team increases score
        if (req.getTeamSize() <= 2) score += 30;
        else if (req.getTeamSize() <= 5) score += 15;
        else score += 5;

        // longer duration reduces complexity slightly
        if (req.getWeeksDuration() <= 4) score += 25;
        else if (req.getWeeksDuration() <= 12) score += 10;
        else score += 5;

        // external integrations add complexity
        score += Math.min(req.getExternalIntegrationsCount() * 10, 30);

        // strict deadlines increase complexity
        if (req.isStrictDeadline()) score += 20;

        if (score < 30) return ComplexityLevel.LOW;
        if (score < 60) return ComplexityLevel.MEDIUM;
        if (score < 85) return ComplexityLevel.HIGH;
        return ComplexityLevel.CRITICAL;
    }
}
