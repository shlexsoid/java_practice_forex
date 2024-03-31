package com.company.currency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/*
This class is crucial for statistics from an optional task.
Fell free to ignore it if you don't do optional part.
Consists of amount of request for each currency.
 */
public class CurrencyDTO {

    @Builder.Default
    @JsonProperty("amount_of_requests")
    private AtomicInteger amountOfRequests = new AtomicInteger();

    public void incAmountOfRequests() {
        amountOfRequests.incrementAndGet();
    }

    public Integer getAmountOfRequests() {
        return amountOfRequests.get();
    }
}
