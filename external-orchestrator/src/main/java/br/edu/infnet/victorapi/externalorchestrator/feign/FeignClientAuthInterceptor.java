package br.edu.infnet.victorapi.externalorchestrator.feign;

import br.edu.infnet.victorapi.externalorchestrator.service.TokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {

    private final TokenService tokenService;

    public FeignClientAuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void apply(RequestTemplate template) {
        String token = tokenService.getToken();
        if (token != null && !token.isBlank()) {
            template.header("Authorization", "Bearer " + token);
        }
    }
}
