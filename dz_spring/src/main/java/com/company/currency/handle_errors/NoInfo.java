package com.company.currency.handle_errors;

public class NoInfo extends RuntimeException {
    @Override
    public String getMessage() {
        return "NoInfo";
    }

    @Override
    public String toString() {
        return "Service cant responde on your request. You can try to get other rate or choose the earlier dates.";
    }
}
