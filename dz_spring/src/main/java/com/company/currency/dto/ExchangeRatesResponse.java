package com.company.currency.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
/*
Class for getting information about exchange rate from the client.
One needs to use it in the service layer to get exchange rate from the client.
 */
public class ExchangeRatesResponse {

    @Builder.Default
    private List<DataSets> dataSets = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataSets {
        private String action;
        private String validFrom;

        private Series series = new Series();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Series {

        @JsonProperty("0:0:0:0:0")
        private FirstSeries firstSeries = new FirstSeries();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FirstSeries {
        private Observations observations = new Observations();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Observations {
        @JsonProperty("0")
        private List<BigDecimal> rate = new ArrayList<>();
    }
}
