package com.company.currency.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class CurrencyUtils {

  public static BigDecimal calcRate(BigDecimal baseRate, BigDecimal targetRate) {
    return targetRate.divide(baseRate, 4, RoundingMode.CEILING);
  }
}
