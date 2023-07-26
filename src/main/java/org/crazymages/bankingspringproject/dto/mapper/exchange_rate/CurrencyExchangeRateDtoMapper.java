package org.crazymages.bankingspringproject.dto.mapper.exchange_rate;

import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDto;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

/**
 * Component class that provides mapping functionality between CurrencyExchangeRate and CurrencyExchangeRateDTO objects.
 */
@Component
public class CurrencyExchangeRateDtoMapper implements DtoMapper<CurrencyExchangeRate, CurrencyExchangeRateDto> {

    @Override
    public CurrencyExchangeRateDto mapEntityToDto(CurrencyExchangeRate currencyExchangeRate) {
        if (currencyExchangeRate == null) {
            throw new IllegalArgumentException("currencyExchangeRate cannot be null");
        }
        return new CurrencyExchangeRateDto(
                currencyExchangeRate.getCurrencyCode(),
                currencyExchangeRate.getExchangeRate()
        );
    }

    @Override
    public CurrencyExchangeRate mapDtoToEntity(CurrencyExchangeRateDto currencyExchangeRateDto) {
        if (currencyExchangeRateDto == null) {
            throw new IllegalArgumentException("currencyExchangeRateDto cannot be null");
        }
        CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();
        currencyExchangeRate.setCurrencyCode(currencyExchangeRateDto.getCurrencyCode());
        currencyExchangeRate.setExchangeRate(currencyExchangeRateDto.getExchangeRate());
        return currencyExchangeRate;
    }
}
