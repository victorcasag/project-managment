package br.edu.infnet.victorapi.externalorchestrator.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "auth-service", url = "http://auth-service:8081", configuration = FeignConfig.class)
public interface AuthFeignClient {

    @PostMapping("/api/v1/auth/token")
    Map<String, Object> token(@RequestBody Map<String, String> body);
}
