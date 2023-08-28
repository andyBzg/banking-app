package org.crazymages.bankingspringproject.dto.exchange_rate.mapper;

import org.crazymages.bankingspringproject.dto.exchange_rate.CurrencyExchangeRateDto;
import org.crazymages.bankingspringproject.dto.exchange_rate.mapper.CurrencyExchangeRateDtoMapper;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangeRateDtoMapperTest {

    CurrencyExchangeRateDtoMapper currencyExchangeRateDtoMapper;
    CurrencyExchangeRateDto currencyExchangeRateDto;
    CurrencyExchangeRate currencyExchangeRate1;
    CurrencyExchangeRate currencyExchangeRate2;

    @BeforeEach
    void setUp() {
        currencyExchangeRateDtoMapper = new CurrencyExchangeRateDtoMapper();
        currencyExchangeRateDto = new CurrencyExchangeRateDto();

        currencyExchangeRate1 = new CurrencyExchangeRate();
        currencyExchangeRate1.setCurrencyCode("EUR");
        currencyExchangeRate1.setExchangeRate(BigDecimal.valueOf(1.2));

        currencyExchangeRate2 = new CurrencyExchangeRate();
        currencyExchangeRate2.setCurrencyCode("USD");
        currencyExchangeRate2.setExchangeRate(BigDecimal.valueOf(0.8));
    }

    @Test
    void mapEntityToDto_validCurrencyExchangeRate_success() {
        // when
        CurrencyExchangeRateDto currencyExchangeRateDto = currencyExchangeRateDtoMapper.mapEntityToDto(currencyExchangeRate1);

        // then
        assertEquals(currencyExchangeRate1.getCurrencyCode(), currencyExchangeRateDto.getCurrencyCode());
        assertEquals(currencyExchangeRate1.getExchangeRate(), currencyExchangeRateDto.getExchangeRate());
    }

    @Test
    void mapEntityToDto_nullCurrencyExchangeRate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> currencyExchangeRateDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validCurrencyExchangeRateDto_success() {
        // given
        currencyExchangeRateDto.setCurrencyCode("EUR");
        currencyExchangeRateDto.setExchangeRate(BigDecimal.valueOf(1.2));

        // when
        CurrencyExchangeRate currencyExchangeRate = currencyExchangeRateDtoMapper.mapDtoToEntity(currencyExchangeRateDto);

        // then
        assertEquals(currencyExchangeRateDto.getCurrencyCode(), currencyExchangeRate.getCurrencyCode());
        assertEquals(currencyExchangeRateDto.getExchangeRate(), currencyExchangeRate.getExchangeRate());
    }

    @Test
    void mapDtoToEntity_nullCurrencyExchangeRateDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> currencyExchangeRateDtoMapper.mapDtoToEntity(null));
    }
}
