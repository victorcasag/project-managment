package br.edu.infnet.victorapi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui.html";
    }

    @GetMapping("/index.html")
    public String redirectIndexToSwagger() {
        return "redirect:/swagger-ui.html";
    }
}