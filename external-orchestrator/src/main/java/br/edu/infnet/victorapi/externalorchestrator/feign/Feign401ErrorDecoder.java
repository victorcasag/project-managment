package br.edu.infnet.victorapi.externalorchestrator.feign;

import br.edu.infnet.victorapi.externalorchestrator.service.TokenService;
import feign.Response;
import feign.codec.ErrorDecoder;

public class Feign401ErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    private final TokenService tokenService;

    public Feign401ErrorDecoder(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 401) {
            // clear token so next attempt triggers refresh
            tokenService.clearToken();
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
