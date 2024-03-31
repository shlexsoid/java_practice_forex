package com.company.currency.handle_errors;

public class CurrencyDoesntExists extends RuntimeException {
    private final String currency;

    public CurrencyDoesntExists(String currency) {
        this.currency = currency;
    }

    @Override
    public String getMessage() {
        return "CurrencyDoesntExists";
    }

    @Override
    public String toString() {
        return String.format("Sorry, but currency (%s) doesn't exist", currency);
    }
}
