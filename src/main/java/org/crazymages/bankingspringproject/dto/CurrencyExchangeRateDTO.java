package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data transfer object (DTO) class representing a Currency exchange rate.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeRateDTO {
    private String currencyCode;
    private BigDecimal exchangeRate;
}
