package br.edu.infnet.victorapi.modules.apyhub;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/apyhub")
public class APyHubController {

    private final APyHubService service;

    public APyHubController(APyHubService service) {
        this.service = service;
    }

    @PostMapping("/{source}/{target}/{date}/convertMoney")
    public ResponseEntity<BigDecimal> convert(
            @PathVariable String source,
            @PathVariable String target,
            @PathVariable String date
    ) {
        BigDecimal result = service.convert(source, target, date);
        if (result == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(result);
    }
}
