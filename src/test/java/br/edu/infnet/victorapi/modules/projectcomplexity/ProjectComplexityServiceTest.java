package br.edu.infnet.victorapi.modules.projectcomplexity;

import br.edu.infnet.victorapi.modules.projectcomplexity.model.ComplexityLevel;
import br.edu.infnet.victorapi.modules.projectcomplexity.model.ProjectComplexityRequest;
import br.edu.infnet.victorapi.modules.projectcomplexity.service.ProjectComplexityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectComplexityServiceTest {

    private final ProjectComplexityService service = new ProjectComplexityService();

    @Test
    @DisplayName("Low complexity for large team, long duration, no integrations")
    public void testLowComplexity() {
        ProjectComplexityRequest req = new ProjectComplexityRequest(10, 24, 0, false);
        ComplexityLevel level = service.evaluate(req);
        assertEquals(ComplexityLevel.LOW, level);
    }

    @Test
    @DisplayName("Medium complexity for medium team and moderate constraints")
    public void testMediumComplexity() {
        ProjectComplexityRequest req = new ProjectComplexityRequest(4, 8, 1, false);
        ComplexityLevel level = service.evaluate(req);
        assertEquals(ComplexityLevel.MEDIUM, level);
    }

    @Test
    @DisplayName("High complexity when strict deadline and multiple integrations")
    public void testHighComplexity() {
        ProjectComplexityRequest req = new ProjectComplexityRequest(3, 6, 2, true);
        ComplexityLevel level = service.evaluate(req);
        assertEquals(ComplexityLevel.HIGH, level);
    }

    @Test
    @DisplayName("Critical complexity for tiny team, short duration, many integrations and strict deadline")
    public void testCriticalComplexity() {
        ProjectComplexityRequest req = new ProjectComplexityRequest(1, 2, 4, true);
        ComplexityLevel level = service.evaluate(req);
        assertEquals(ComplexityLevel.CRITICAL, level);
    }

    @Test
    @DisplayName("IllegalArgumentException for negative numbers")
    public void testInvalidInputs() {
        ProjectComplexityRequest req = new ProjectComplexityRequest(-1, 2, 0, false);
        assertThrows(IllegalArgumentException.class, () -> service.evaluate(req));
    }
}
