package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDTO;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class CurrencyExchangeRateDTOMapper implements DTOMapper<CurrencyExchangeRate, CurrencyExchangeRateDTO> {

    @Override
    public CurrencyExchangeRateDTO mapEntityToDto(CurrencyExchangeRate currencyExchangeRate) {
        return new CurrencyExchangeRateDTO(
                currencyExchangeRate.getCurrencyCode(),
                currencyExchangeRate.getExchangeRate()
        );
    }

    @Override
    public CurrencyExchangeRate mapDtoToEntity(CurrencyExchangeRateDTO currencyExchangeRateDTO) {
        CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();
        currencyExchangeRate.setCurrencyCode(currencyExchangeRateDTO.getCurrencyCode());
        currencyExchangeRate.setExchangeRate(currencyExchangeRateDTO.getExchangeRate());
        return currencyExchangeRate;
    }

    @Override
    public List<CurrencyExchangeRateDTO> getListOfDTOs(List<CurrencyExchangeRate> clientList) {
        return Optional.ofNullable(clientList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
