package br.edu.infnet.victorapi.modules.external.controller;

import br.edu.infnet.victorapi.modules.external.service.NameInsightService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external")
public class NameInsightController {

    private final NameInsightService service;

    public NameInsightController(NameInsightService service) {
        this.service = service;
    }

    @GetMapping("/name-insight")
    public String get(@RequestParam("name") String name) {
        return service.describeName(name);
    }
}
