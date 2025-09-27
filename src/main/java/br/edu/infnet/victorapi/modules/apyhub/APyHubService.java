package br.edu.infnet.victorapi.modules.apyhub;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class APyHubService {

    private final APyHubClient client;

    public APyHubService(APyHubClient client) {
        this.client = client;
    }

    public BigDecimal convert(String source, String target, String date) {
        try {
            return client.convert(source, target, date);
        } catch (Exception e) {
            return null;
        }
    }
}
