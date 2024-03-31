package com.company.currency.service;

import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.dto.CurrencyDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ICurrencyRateService {
    List<String> CURRENCY_LIST =
        List.of(
            "USD", "JPY", "BGN", "CZK", "DKK", "GBP", "HUF", "PLN", "RON", "SEK", "CHF", "ISK",
            "NOK", "HRK", "RUB", "TRY", "AUD", "BRL", "CAD", "CNY", "HKD", "IDR", "ILS", "INR",
            "KRW", "MXN", "MYR", "NZD", "PHP", "SGD", "THB", "ZAR", "EUR");

    CurrencyRateDTO getRate(String base, String target, BigDecimal amount, LocalDate date);

    // OPTIONAL AS A BONUS TASK
  Map<String, CurrencyDTO> getStatistics();
}
