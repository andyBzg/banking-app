package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDTO;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.CurrencyExchangeRateRepository;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.CurrencyExchangeRateDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeRateDatabaseServiceImpl implements CurrencyExchangeRateDatabaseService {

    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final CurrencyExchangeRateDTOMapper currencyExchangeRateDTOMapper;


    @Override
    public void create(CurrencyExchangeRate currencyExchangeRate) {
        currencyExchangeRateRepository.saveAndFlush(currencyExchangeRate);
    }

    @Override
    @Transactional
    public List<CurrencyExchangeRate> findAll() {
        return currencyExchangeRateRepository.findAllNotDeleted();
    }

    @Override
    public List<CurrencyExchangeRateDTO> findAllDTOs() {
        return currencyExchangeRateDTOMapper.getListOfDTOs(
                currencyExchangeRateRepository.findAllNotDeleted());
    }

    @Override
    @Transactional
    public CurrencyExchangeRate findById(String currencyCode) {
        return currencyExchangeRateRepository.findById(currencyCode)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new DataNotFoundException(currencyCode));
    }

    @Override
    @Transactional
    public void update(String currencyCode, CurrencyExchangeRate currencyExchangeRate) {
        CurrencyExchangeRate rate = currencyExchangeRateRepository.findById(currencyCode)
                .orElseThrow(() -> new DataNotFoundException(currencyCode));

        if (!rate.isDeleted()) {
            rate.setCurrencyCode(currencyExchangeRate.getCurrencyCode());
            rate.setExchangeRate(currencyExchangeRate.getExchangeRate());
            currencyExchangeRateRepository.save(rate);
        }
    }

    @Override
    @Transactional
    public void delete(String currencyCode) {
        CurrencyExchangeRate rate = currencyExchangeRateRepository.findById(currencyCode)
                .orElseThrow(() -> new DataNotFoundException(currencyCode));
        rate.setDeleted(true);
        currencyExchangeRateRepository.save(rate);
    }
}
