package br.edu.infnet.victorapi.authservice.controller;

import br.edu.infnet.victorapi.authservice.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class TokenController {

    private final JwtService jwtService;

    public TokenController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody Map<String, String> body) {
        var grantType = body.getOrDefault("grant_type", "client_credentials");
        if ("client_credentials".equals(grantType)) {
            String clientId = body.get("client_id");
            String clientSecret = body.get("client_secret");
            // development-only check against application.yml hardcoded client
            if ("service-client".equals(clientId) && "service-secret".equals(clientSecret)) {
                var token = jwtService.generateToken(clientId);
                return ResponseEntity.ok(Map.of("access_token", token, "expires_in", jwtService.getExpirationMillis()));
            }
            return ResponseEntity.status(401).body(Map.of("error", "invalid_client"));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "unsupported_grant_type"));
    }
}
