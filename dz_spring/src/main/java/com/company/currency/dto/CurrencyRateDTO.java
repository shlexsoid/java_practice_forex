package com.company.currency.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/* This annotations from lombok library. It helps to avoid write getters, setters and constructor
 manually. Instead, mark classes by annotations.
 For more information check https://projectlombok.org
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
/*
This dao class consist of information about exchange rate, converted amount and description of pair.
 */
public class CurrencyRateDTO {

    private BigDecimal rate;

    @JsonProperty("converted_amount")
    private BigDecimal convertedAmount;

    private String pair;

    @JsonProperty("chart_link")
    private String chartLink;
}
