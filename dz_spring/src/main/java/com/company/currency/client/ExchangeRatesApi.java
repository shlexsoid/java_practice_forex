package com.company.currency.client;

import com.company.currency.dto.ExchangeRatesResponse;
import java.time.LocalDate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

// FeignClient for communication with client by @url.
@FeignClient(value = "ecb-data-warehouse", url = "https://sdw-wsrest.ecb.europa.eu")
/*
This class requires Spring Cloud knowledge, so already implemented.
 */
public interface ExchangeRatesApi {

  @GetMapping(
      value =
          "/service/data/EXR/D.{currency}.EUR.SP00.A",
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<ExchangeRatesResponse> getCurrencyRate(
      @RequestParam("startPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDay,
      @RequestParam("endPeriod") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDay,
      @PathVariable("currency") String currency);
}
