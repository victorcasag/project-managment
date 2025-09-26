package br.edu.infnet.victorapi.authservice.controller;

import br.edu.infnet.victorapi.authservice.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        var username = body.get("username");
        var password = body.get("password");

        // For the exercise we'll accept hardcoded user/passwords
        if ("admin".equals(username) && "adminpass".equals(password)) {
            var token = jwtService.generateToken(username);
            return ResponseEntity.ok(Map.of("token", token, "expiresIn", jwtService.getExpirationMillis()));
        }

        return ResponseEntity.status(401).body(Map.of("error", "invalid_credentials"));
    }
}
