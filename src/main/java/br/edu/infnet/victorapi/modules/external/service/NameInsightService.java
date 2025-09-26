package br.edu.infnet.victorapi.modules.external.service;

import br.edu.infnet.victorapi.modules.external.client.AgifyClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NameInsightService {

    private final AgifyClient agifyClient;

    public NameInsightService(AgifyClient agifyClient) {
        this.agifyClient = agifyClient;
    }

    public String describeName(String name) {
        Map<String, Object> res = agifyClient.predict(name);
        Object age = res.getOrDefault("age", "unknown");
    Object count = res.getOrDefault("count", "?");
        return String.format("Name '%s' estimated age: %s (count=%s)", name, age, count);
    }
}
