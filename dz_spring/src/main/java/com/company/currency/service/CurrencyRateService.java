package com.company.currency.service;

import com.company.currency.client.ExchangeRatesApi;
import com.company.currency.dto.CurrencyDTO;
import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.dto.ExchangeRatesResponse;
import com.company.currency.handle_errors.CurrencyDoesntExists;
import com.company.currency.handle_errors.NoInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.company.currency.util.CurrencyUtils.calcRate;

// @Slf4j - for logging.
@Slf4j
@Service
public class CurrencyRateService implements ICurrencyRateService {
  @Autowired
  private ExchangeRatesApi api;

  Map<String, CurrencyDTO> statistics;

  @Override
  public CurrencyRateDTO getRate(String base, String target, BigDecimal amount, LocalDate date) {
    BigDecimal rate;

    if (!CURRENCY_LIST.contains(base)) { // Если base валюты в списках валюты
        throw new CurrencyDoesntExists(base);
      }

    if (!CURRENCY_LIST.contains(target)) { // Если target валюты в списках валюты
      throw new CurrencyDoesntExists(target);
    }

    if (statistics == null) { // Если статистику еще не вели
      beginCount();
    }

    statistics.get(base).incAmountOfRequests();

    if (!base.equals(target)) {
      statistics.get(target).incAmountOfRequests();
    }

    if (base.equals(target)) {
      rate = BigDecimal.ONE;
    } else if (base.equals("EUR")) {
      ExchangeRatesResponse targetResponse = api.getCurrencyRate(date, date, target).getBody();

      rate = getRateToEurFromResponse(targetResponse);
    } else if (target.equals("EUR")) {
      ExchangeRatesResponse baseResponse = api.getCurrencyRate(date, date, base).getBody();

      BigDecimal baseRateToEur = getRateToEurFromResponse(baseResponse);
      rate = calcRate(baseRateToEur, BigDecimal.ONE);
    } else {
      ExchangeRatesResponse targetResponse = api.getCurrencyRate(date, date, target).getBody();
      ExchangeRatesResponse baseResponse = api.getCurrencyRate(date, date, base).getBody();

      BigDecimal baseRateToEur = getRateToEurFromResponse(baseResponse);
      BigDecimal targetRateToEur = getRateToEurFromResponse(targetResponse);

      rate = calcRate(baseRateToEur, targetRateToEur);
    }

    CurrencyRateDTO answer = new CurrencyRateDTO();
    answer.setRate(rate);
    answer.setPair(String.format("%s/%s", target, base));
    if (amount != null) {
      answer.setConvertedAmount(amount.multiply(rate));
    }

    return answer;
  }

  @Override
  public Map<String, CurrencyDTO> getStatistics() {
    if (statistics == null) {
      beginCount();
    }
    return statistics;
  }

  private void beginCount() {
    statistics = new ConcurrentHashMap<>();
    for (String cur : CURRENCY_LIST) {
      statistics.put(cur, new CurrencyDTO(new AtomicInteger(0)));
    }
  }

  private BigDecimal getRateToEurFromResponse(ExchangeRatesResponse response) {
    List<BigDecimal> rates;
    try {
      rates = response
              .getDataSets()
              .get(0)
              .getSeries().getFirstSeries()
              .getObservations()
              .getRate();
    } catch (Exception ex) {
      throw new NoInfo();
    }

    return rates.get(0);
  }
}
