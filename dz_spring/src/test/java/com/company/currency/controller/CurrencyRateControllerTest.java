package com.company.currency.controller;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.company.currency.client.ExchangeRatesApi;
import com.company.currency.dto.CurrencyDTO;
import com.company.currency.dto.CurrencyRateDTO;
import com.company.currency.dto.ErrorResponse;
import com.company.currency.dto.ExchangeRatesResponse;
import com.company.currency.service.CurrencyRateService;
import com.company.currency.testdata.TestData;
import com.company.currency.util.CurrencyUtils;
import io.restassured.http.ContentType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyRateControllerTest {

    @Value("http://localhost:${local.server.port}/api/exchange-rate")
    private String baseUrl;

    @MockBean private ExchangeRatesApi exchangeRatesApi;

//  OPTIONAL SERVICE FOR STATISTICS TEST
  @Autowired
  private CurrencyRateService currencyRateService;

    private ResponseEntity<ExchangeRatesResponse> exchangeRatesResponse;
    private LocalDate day;
    private BigDecimal rate;

    @BeforeEach
    void setUp() {
        day = LocalDate.now().minusDays(1);
        rate = BigDecimal.valueOf(3.2);
        exchangeRatesResponse = TestData.getExchangeRatesApiResponse(day, rate);
    }

    @Test
    void test_EUR_USD_Rate() {
        String pair = "USD/EUR";
        Mockito.when(
            exchangeRatesApi.getCurrencyRate(Mockito.any(), Mockito.any(), Mockito.anyString()))
            .thenReturn(exchangeRatesResponse);

        CurrencyRateDTO response =
            given()
                .contentType(ContentType.JSON)
                .baseUri(baseUrl)
                .queryParam("day", day.format(DateTimeFormatter.ISO_DATE))
                .when()
                .get("/{target}/{base}", "USD", "EUR")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(CurrencyRateDTO.class);
        assertEquals(CurrencyUtils.calcRate(rate, BigDecimal.ONE), response.getRate());
        assertEquals(response.getPair(), pair);
    }

    @Test
    void test_Not_Exist_Currency() {
        ErrorResponse response =
            given()
                .accept(ContentType.JSON)
                .baseUri(baseUrl)
                .when()
                .get("/{target}/{base}", "ZFR", "EUR")
                .prettyPeek()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .body()
                .as(ErrorResponse.class);
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
    }

//  OPTIONAL TEST FOR STATISTICS
  @Test
  void test_Statistics() {
    currencyRateService.getStatistics().get("USD").incAmountOfRequests();

    Map<String, CurrencyDTO> response =
        given()
            .contentType(ContentType.JSON)
            .baseUri(baseUrl)
            .when()
            .get("/statistics")
            .prettyPeek()
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .jsonPath()
            .getMap("");
    assertEquals(currencyRateService.getStatistics().size(), response.size());
    assertTrue(currencyRateService.getStatistics().get("USD").getAmountOfRequests() >= 1);
  }
}
