package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDto;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

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
    void mapEntityToDto_nullCurrencyExchangeRate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> currencyExchangeRateDtoMapper.mapEntityToDto(null));
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
    void mapDtoToEntity_nullCurrencyExchangeRateDto_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> currencyExchangeRateDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void getDtoList_validCurrencyExchangeRateList_success() {
        // given
        List<CurrencyExchangeRate> currencyExchangeRateList = List.of(currencyExchangeRate1, currencyExchangeRate2);

        // when
        List<CurrencyExchangeRateDto> actual = currencyExchangeRateDtoMapper.getDtoList(currencyExchangeRateList);

        // then
        assertEquals(2, actual.size());

        CurrencyExchangeRateDto currencyExchangeRateDto1 = actual.get(0);
        assertEquals(currencyExchangeRate1.getCurrencyCode(), currencyExchangeRateDto1.getCurrencyCode());
        assertEquals(currencyExchangeRate1.getExchangeRate(), currencyExchangeRateDto1.getExchangeRate());

        CurrencyExchangeRateDto currencyExchangeRateDto2 = actual.get(1);
        assertEquals(currencyExchangeRate2.getCurrencyCode(), currencyExchangeRateDto2.getCurrencyCode());
        assertEquals(currencyExchangeRate2.getExchangeRate(), currencyExchangeRateDto2.getExchangeRate());
    }

    @Test
    void getDtoList_nullCurrencyExchangeRateList_throwsDataNotFoundException() {
        assertThrows(DataNotFoundException.class, () -> currencyExchangeRateDtoMapper.getDtoList(null));
    }

    @Test
    void getDtoList_emptyCurrencyExchangeRateList_returnsEmptyList() {
        // given
        List<CurrencyExchangeRate> currencyExchangeRateList = Collections.emptyList();

        // when
        List<CurrencyExchangeRateDto> actual = currencyExchangeRateDtoMapper.getDtoList(currencyExchangeRateList);

        // then
        assertTrue(actual.isEmpty());
    }
}
