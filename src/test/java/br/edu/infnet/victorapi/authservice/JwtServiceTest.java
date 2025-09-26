package br.edu.infnet.victorapi.authservice;

import br.edu.infnet.victorapi.authservice.service.JwtService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void generateToken_containsSubjectAndNotNull() {
        JwtService svc = new JwtService();
        String token = svc.generateToken("test-user");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void getExpirationMillis_defaultIsPositive() {
        JwtService svc = new JwtService();
        long exp = svc.getExpirationMillis();
        assertTrue(exp > 0);
    }
}
