package br.edu.infnet.victorapi.modules.external.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "agifyClient", url = "https://api.agify.io")
public interface AgifyClient {

    @GetMapping
    Map<String, Object> predict(@RequestParam("name") String name);
}
