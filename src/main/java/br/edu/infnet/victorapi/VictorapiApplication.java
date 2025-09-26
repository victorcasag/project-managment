package br.edu.infnet.victorapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties
@EnableFeignClients
public class VictorapiApplication {
    public static void main(String[] args) {
        SpringApplication.run(VictorapiApplication.class, args);
    }
}