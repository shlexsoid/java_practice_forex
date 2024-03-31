package com.company.currency.controller;

import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.dto.CurrencyDTO;
import com.company.currency.service.ICurrencyRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Map;


public class CurrencyRateController implements CurrencyRateApi {

  @Autowired
  private ICurrencyRateService service;

  @GetMapping("/{target}/{base}")
  @Override
  public CurrencyRateDTO getRate(
          @PathVariable String base,
          @PathVariable String target,
          @RequestParam(required = false) BigDecimal amount,
          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> day) {
    LocalDate date = day.orElse(LocalDate.now().minusDays(2));
    return service.getRate(base, target, amount, date);
  }

  @GetMapping("/statistics")
  @Override
  public Map<String, CurrencyDTO> getCurrencyStatistics() {
    return service.getStatistics();
  }
}
