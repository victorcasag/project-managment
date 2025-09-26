package br.edu.infnet.victorapi.externalorchestrator.service;

import br.edu.infnet.victorapi.externalorchestrator.feign.AuthFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

class TokenServiceTest {

    private AuthFeignClient authFeignClient;
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        authFeignClient = Mockito.mock(AuthFeignClient.class);
        tokenService = new TokenService(authFeignClient);
    }

    @Test
    void getToken_whenNoCached_thenRequestsToken() {
        when(authFeignClient.token(anyMap())).thenReturn(Map.of("access_token", "abc123", "expires_in", 3600000));

        String token = tokenService.getToken();
        assertEquals("abc123", token);

        // second call should use cache
        String token2 = tokenService.getToken();
        assertEquals("abc123", token2);
        verify(authFeignClient, times(1)).token(anyMap());
    }

    @Test
    void refreshToken_onError_clearsAndReturnsEmpty() {
        when(authFeignClient.token(anyMap())).thenThrow(new RuntimeException("down"));

        String token = tokenService.refreshToken();
        assertTrue(token.isBlank());
    }

    @Test
    void clearToken_removesCache() {
        when(authFeignClient.token(anyMap())).thenReturn(Map.of("access_token", "t1", "expires_in", 1));
        String token = tokenService.getToken();
        assertEquals("t1", token);

        // clear and force refresh to attempt new call which will return same mocked token
        tokenService.clearToken();
        when(authFeignClient.token(anyMap())).thenReturn(Map.of("access_token", "t2", "expires_in", 3600000));
        String token2 = tokenService.getToken();
        assertEquals("t2", token2);
    }
}
