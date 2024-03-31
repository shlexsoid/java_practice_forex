package com.company.currency.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.company.currency.client.ExchangeRatesApi;
import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.dto.ExchangeRatesResponse;
import com.company.currency.testdata.TestData;
import com.company.currency.util.CurrencyUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceTest {

    @InjectMocks private CurrencyRateService currencyRateService;

    @Mock private ExchangeRatesApi exchangeRatesApi;

    @Test
    void test_EUR_USD_Rate() {
        String pair = String.format("%s/%s", "EUR", "USD");
        LocalDate day = LocalDate.now().minusDays(1);
        BigDecimal rate = BigDecimal.valueOf(3.2);
        ResponseEntity<ExchangeRatesResponse> exchangeRatesResponse =
            TestData.getExchangeRatesApiResponse(day, rate);

        Mockito.when(
            exchangeRatesApi.getCurrencyRate(Mockito.any(), Mockito.any(), Mockito.anyString()))
            .thenReturn(exchangeRatesResponse);

        CurrencyRateDTO currencyRateDTO = currencyRateService.getRate("EUR", "USD", null, day);

        assertEquals(rate, currencyRateDTO.getRate());
        assertEquals(pair, currencyRateDTO.getPair());
    }

    @Test
    void test_USD_EUR_Rate() {
        String pair = String.format("%s/%s", "USD", "EUR");
        LocalDate day = LocalDate.now().minusDays(1);
        BigDecimal rate = BigDecimal.valueOf(3.2);
        ResponseEntity<ExchangeRatesResponse> exchangeRatesResponse =
            TestData.getExchangeRatesApiResponse(day, rate);

        Mockito.when(
            exchangeRatesApi.getCurrencyRate(Mockito.any(), Mockito.any(), Mockito.anyString()))
            .thenReturn(exchangeRatesResponse);

        CurrencyRateDTO currencyRateDTO = currencyRateService.getRate("USD", "EUR", null, day);

        assertEquals(CurrencyUtils.calcRate(rate, BigDecimal.ONE), currencyRateDTO.getRate());
        assertEquals(pair, currencyRateDTO.getPair());
    }

    @Test
    void test_USD_USD() {
        LocalDate day = LocalDate.now().minusDays(1);
        CurrencyRateDTO currencyRateDTO = currencyRateService.getRate("USD", "USD", null, day);

        assertEquals(BigDecimal.ONE, currencyRateDTO.getRate());
        assertEquals("USD/USD", currencyRateDTO.getPair());
    }

    @Test
    void test_USD_HUF() {
        LocalDate date = LocalDate.now().minusDays(1);
        String pair = String.format("%s/%s", "HUF", "USD");

        BigDecimal rateUSD = BigDecimal.valueOf(3.2);
        BigDecimal rateHUF = BigDecimal.valueOf(10);
        ResponseEntity<ExchangeRatesResponse> responseUSD =
            TestData.getExchangeRatesApiResponse(date, rateUSD);

        ResponseEntity<ExchangeRatesResponse> responseHUF =
            TestData.getExchangeRatesApiResponse(date, rateHUF);
        Mockito.when(exchangeRatesApi.getCurrencyRate(date, date, "USD")).thenReturn(responseUSD);
        Mockito.when(exchangeRatesApi.getCurrencyRate(date, date, "HUF")).thenReturn(responseHUF);

        CurrencyRateDTO rateDTO = currencyRateService.getRate("HUF", "USD", null, date);

        assertEquals(pair, rateDTO.getPair());
        assertEquals(CurrencyUtils.calcRate(rateHUF, rateUSD), rateDTO.getRate());
    }

    @Test
    void test_Convert_USD_HUF() {
        LocalDate date = LocalDate.now().minusDays(1);
        String pair = String.format("%s/%s", "HUF", "USD");

        BigDecimal rateUSD = BigDecimal.valueOf(3.2);
        BigDecimal rateHUF = BigDecimal.valueOf(10);

        BigDecimal expectedRate = CurrencyUtils.calcRate(rateHUF, rateUSD);
        BigDecimal amount = BigDecimal.valueOf(20);
        BigDecimal convertedAmount = expectedRate.multiply(amount);

        ResponseEntity<ExchangeRatesResponse> responseUSD =
            TestData.getExchangeRatesApiResponse(date, rateUSD);

        ResponseEntity<ExchangeRatesResponse> responseHUF =
            TestData.getExchangeRatesApiResponse(date, rateHUF);
        Mockito.when(exchangeRatesApi.getCurrencyRate(date, date, "USD")).thenReturn(responseUSD);
        Mockito.when(exchangeRatesApi.getCurrencyRate(date, date, "HUF")).thenReturn(responseHUF);

        CurrencyRateDTO rateDTO = currencyRateService.getRate("HUF", "USD", amount, date);

        assertEquals(pair, rateDTO.getPair());
        assertEquals(expectedRate, rateDTO.getRate());
        assertEquals(convertedAmount, rateDTO.getConvertedAmount());
        assertNotNull(rateDTO.getChartLink());
    }
}
