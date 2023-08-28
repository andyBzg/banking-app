package org.crazymages.bankingspringproject.dto.exchange_rate;

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
public class CurrencyExchangeRateDto {
    private String currencyCode;
    private BigDecimal exchangeRate;
}
