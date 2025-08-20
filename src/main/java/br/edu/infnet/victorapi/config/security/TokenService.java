package br.edu.infnet.victorapi.config.security;

import br.edu.infnet.victorapi.modules.users.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Autowired
    private SecurityProperties securityProperties;
    
    private String getSecret() {
        return securityProperties.getToken().getSecret();
    }

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSecret());
            return JWT.create()
                    .withIssuer("victorapi")
                    .withSubject(user.getEmail())
                    .withClaim("userId", user.getId())
                    .withClaim("name", user.getName())
                    .withClaim("role", user.getRole().toString())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSecret());
            var verifier = JWT.require(algorithm)
                    .withIssuer("victorapi")
                    .build();

            var decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject(); // Retorna o email
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!", exception);
        }
    }

    public Integer getUserIdFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSecret());
            var verifier = JWT.require(algorithm)
                    .withIssuer("victorapi")
                    .build();

            var decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("userId").asInt();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!", exception);
        }
    }

    public String getUserRoleFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSecret());
            var verifier = JWT.require(algorithm)
                    .withIssuer("victorapi")
                    .build();

            var decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("role").asString();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!", exception);
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSecret());
            var verifier = JWT.require(algorithm)
                    .withIssuer("victorapi")
                    .build();

            var decodedJWT = verifier.verify(token);
            return decodedJWT.getExpiresAt().before(java.util.Date.from(Instant.now()));
        } catch (JWTVerificationException exception) {
            return true;
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(getSecret());
            return JWT.create()
                    .withIssuer("victorapi")
                    .withSubject(user.getEmail())
                    .withClaim("type", "refresh")
                    .withClaim("userId", user.getId())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(genRefreshExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar refresh token", exception);
        }
    }

    private Instant genRefreshExpirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));
    }
}