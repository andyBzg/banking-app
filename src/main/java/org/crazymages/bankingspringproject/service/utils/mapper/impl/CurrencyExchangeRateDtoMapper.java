package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDto;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between CurrencyExchangeRate and CurrencyExchangeRateDTO objects.
 */
@Component
public class CurrencyExchangeRateDtoMapper implements DtoMapper<CurrencyExchangeRate, CurrencyExchangeRateDto> {

    @Override
    public CurrencyExchangeRateDto mapEntityToDto(CurrencyExchangeRate currencyExchangeRate) {
        return new CurrencyExchangeRateDto(
                currencyExchangeRate.getCurrencyCode(),
                currencyExchangeRate.getExchangeRate()
        );
    }

    @Override
    public CurrencyExchangeRate mapDtoToEntity(CurrencyExchangeRateDto currencyExchangeRateDto) {
        CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();
        currencyExchangeRate.setCurrencyCode(currencyExchangeRateDto.getCurrencyCode());
        currencyExchangeRate.setExchangeRate(currencyExchangeRateDto.getExchangeRate());
        return currencyExchangeRate;
    }

    @Override
    public List<CurrencyExchangeRateDto> getDtoList(List<CurrencyExchangeRate> clientList) {
        return Optional.ofNullable(clientList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
