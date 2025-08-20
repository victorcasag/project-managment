package br.edu.infnet.victorapi.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "api.security")
public class SecurityProperties {
    
    private Token token = new Token();
    
    public Token getToken() {
        return token;
    }
    
    public void setToken(Token token) {
        this.token = token;
    }
    
    public static class Token {
        private String secret = "defaultSecretKey123456789012345678901234567890";
        
        public String getSecret() {
            return secret;
        }
        
        public void setSecret(String secret) {
            this.secret = secret;
        }
    }
}
