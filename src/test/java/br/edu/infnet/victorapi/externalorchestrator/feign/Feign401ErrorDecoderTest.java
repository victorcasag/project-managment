package br.edu.infnet.victorapi.externalorchestrator.feign;

import br.edu.infnet.victorapi.externalorchestrator.service.TokenService;
import feign.Response;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.mockito.Mockito.*;

class Feign401ErrorDecoderTest {

    @Test
    void decode_401_clearsToken() {
        TokenService tokenService = Mockito.mock(TokenService.class);
        Feign401ErrorDecoder d = new Feign401ErrorDecoder(tokenService);

        Response r = Response.builder()
                .status(401)
                .request(Request.create(Request.HttpMethod.GET, "/x", Collections.emptyMap(), null, null))
                .build();

        d.decode("m", r);
        verify(tokenService, times(1)).clearToken();
    }
}
