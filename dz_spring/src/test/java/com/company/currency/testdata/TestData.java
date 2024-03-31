package com.company.currency.testdata;

import com.company.currency.dto.ExchangeRatesResponse;
import com.company.currency.dto.ExchangeRatesResponse.DataSets;
import com.company.currency.dto.ExchangeRatesResponse.FirstSeries;
import com.company.currency.dto.ExchangeRatesResponse.Observations;
import com.company.currency.dto.ExchangeRatesResponse.Series;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public class TestData {

    public static ResponseEntity<ExchangeRatesResponse> getExchangeRatesApiResponse(LocalDate day, BigDecimal rate) {
        return ResponseEntity.ok(ExchangeRatesResponse.builder()
            .dataSets(
                List.of(new DataSets(
                    "Replace",
                    day.format(DateTimeFormatter.ISO_DATE),
                    new Series(new FirstSeries(new Observations(List.of(rate)))))))
            .build());
    }
}
