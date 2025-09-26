package br.edu.infnet.victorapi.modules.external;

import br.edu.infnet.victorapi.modules.external.client.AgifyClient;
import br.edu.infnet.victorapi.modules.external.service.NameInsightService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NameInsightServiceTest {

    @Test
    @DisplayName("Service composes response from Feign client")
    public void testDescribeName() {
        AgifyClient client = mock(AgifyClient.class);
        Map<String, Object> resp = new HashMap<>();
        resp.put("age", 30);
        resp.put("count", 1000);
        when(client.predict("victor")).thenReturn(resp);

        NameInsightService svc = new NameInsightService(client);
        String out = svc.describeName("victor");
        assertTrue(out.contains("victor") && out.contains("30") );
    }
}
