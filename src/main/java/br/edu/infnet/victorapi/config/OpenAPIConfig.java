package br.edu.infnet.victorapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${server.port:8080}")
    private int serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("VictorAPI - Sistema de Gerenciamento de Projetos")
                        .version("1.0.0")
                        .description("""
                            ## API para Gerenciamento de Projetos
                        
                            ### Como usar:
                            1. Crie um usuario em `/api/v1/auth/register`
                            2. Faça login em `/api/v1/auth/login`
                            3. Copie o token retornado
                            4. Clique em "Authorize" e cole o token
                            5. Agora você pode testar todos os endpoints!
                            """)
                        .contact(new Contact()
                                .name("Victor - Portfólio ")
                                .email("victor.casagrande@al.infnet.edu.br")
                                .url("https://victorcasagrande.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("🖥️ Servidor de Desenvolvimento")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o token JWT (sem 'Bearer ')")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/api/**")
                .packagesToScan("br.edu.infnet.victorapi")
                .build();
    }
}