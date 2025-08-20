package br.edu.infnet.victorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class VictorapiApplication {
    public static void main(String[] args) {
        SpringApplication.run(VictorapiApplication.class, args);
    }
}