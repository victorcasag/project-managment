package br.edu.infnet.victorapi.externalorchestrator.feign;

import br.edu.infnet.victorapi.externalorchestrator.service.TokenService;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class FeignClientAuthInterceptorTest {

    private TokenService tokenService;
    private FeignClientAuthInterceptor interceptor;

    @BeforeEach
    void setup() {
        tokenService = Mockito.mock(TokenService.class);
        interceptor = new FeignClientAuthInterceptor(tokenService);
    }

    @Test
    void apply_attachesHeaderWhenTokenPresent() {
        when(tokenService.getToken()).thenReturn("abc-123");
        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);
        assert template.headers().get("Authorization").iterator().next().contains("abc-123");
    }

    @Test
    void apply_noHeaderWhenTokenEmpty() {
        when(tokenService.getToken()).thenReturn("");
        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);
        assert template.headers().get("Authorization") == null || template.headers().get("Authorization").isEmpty();
    }
}
