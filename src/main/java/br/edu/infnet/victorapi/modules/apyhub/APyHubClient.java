package br.edu.infnet.victorapi.modules.apyhub;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "apyhubClient", url = "${URL_PART_TWO}")
public interface APyHubClient {

    @GetMapping("/api/apyhub/{source}/{target}/{date}/convertMoney")
    BigDecimal convert(@PathVariable("source") String source,
                       @PathVariable("target") String target,
                       @PathVariable("date") String date);
}
