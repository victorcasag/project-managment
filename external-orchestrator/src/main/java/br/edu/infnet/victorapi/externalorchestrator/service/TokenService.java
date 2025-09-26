package br.edu.infnet.victorapi.externalorchestrator.service;

import br.edu.infnet.victorapi.externalorchestrator.feign.AuthFeignClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class TokenService {

    private final AuthFeignClient authFeignClient;

    private final AtomicReference<CachedToken> cached = new AtomicReference<>();

    public TokenService(AuthFeignClient authFeignClient) {
        this.authFeignClient = authFeignClient;
    }

    public String getToken() {
        var c = cached.get();
        if (c != null && c.expiresAt.isAfter(Instant.now().plusSeconds(30))) {
            return c.token;
        }
        return refreshToken();
    }

    public synchronized String refreshToken() {
        try {
            Map<String, String> body = Map.of(
                    "grant_type", "client_credentials",
                    "client_id", "service-client",
                    "client_secret", "service-secret"
            );
            Map<String, Object> resp = authFeignClient.token(body);
            Object tokenObj = resp.get("access_token");
            if (tokenObj == null) return "";
            String token = tokenObj.toString();
            long expiresIn = 3600000L;
            Object expiresObj = resp.get("expires_in");
            if (expiresObj instanceof Number) {
                expiresIn = ((Number) expiresObj).longValue();
            } else if (resp.get("expiresIn") instanceof Number) {
                expiresIn = ((Number) resp.get("expiresIn")).longValue();
            }
            var exp = Instant.now().plusMillis(expiresIn);
            cached.set(new CachedToken(token, exp));
            return token;
        } catch (Exception e) {
            cached.set(null);
            return "";
        }
    }

    public void clearToken() {
        cached.set(null);
    }

    record CachedToken(String token, Instant expiresAt) {}
}
