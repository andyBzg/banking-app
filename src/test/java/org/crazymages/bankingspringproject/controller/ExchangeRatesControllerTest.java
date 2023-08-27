package org.crazymages.bankingspringproject.controller;

import org.crazymages.bankingspringproject.dto.exchange_rate.CurrencyExchangeRateDto;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRatesControllerTest {

    @Mock
    CurrencyExchangeRateDatabaseService currencyExchangeRateDatabaseService;

    @InjectMocks
    ExchangeRatesController exchangeRatesController;

    @Test
    void getExchangeRates_success() {
        // given
        List<CurrencyExchangeRateDto> expected = List.of(
                new CurrencyExchangeRateDto("USD", BigDecimal.valueOf(1.0)),
                new CurrencyExchangeRateDto("EUR", BigDecimal.valueOf(0.89))
        );
        when(currencyExchangeRateDatabaseService.findAllRates()).thenReturn(expected);

        // when
        ResponseEntity<List<CurrencyExchangeRateDto>> actual = exchangeRatesController.getExchangeRates();

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(currencyExchangeRateDatabaseService).findAllRates();
    }

    @Test
    void getExchangeRates_emptyList_returnsNoContentStatus() {
        // given
        List<CurrencyExchangeRateDto> expected = Collections.emptyList();
        when(currencyExchangeRateDatabaseService.findAllRates()).thenReturn(expected);

        // when
        ResponseEntity<List<CurrencyExchangeRateDto>> actual = exchangeRatesController.getExchangeRates();

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(currencyExchangeRateDatabaseService).findAllRates();
    }
}
