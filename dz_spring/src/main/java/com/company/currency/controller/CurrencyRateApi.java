package com.company.currency.controller;

import com.company.currency.dto.CurrencyDTO;
import com.company.currency.dto.CurrencyRateDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface CurrencyRateApi {

    CurrencyRateDTO getRate(String base, String target, BigDecimal amount, Optional<LocalDate> day);
//      OPTIONAL
    Map<String, CurrencyDTO> getCurrencyStatistics();
}
