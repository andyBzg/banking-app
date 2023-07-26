package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDto;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.CurrencyExchangeRateRepository;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.crazymages.bankingspringproject.dto.mapper.exchange_rate.CurrencyExchangeRateDtoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A service implementation for managing Currency exchange rate entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyExchangeRateDatabaseServiceImpl implements CurrencyExchangeRateDatabaseService {

    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final CurrencyExchangeRateDtoMapper currencyExchangeRateDtoMapper;


    @Override
    @Transactional
    public void create(CurrencyExchangeRate currencyExchangeRate) {
        String currencyCode = currencyExchangeRate.getCurrencyCode();
        BigDecimal exchangeRate = currencyExchangeRate.getExchangeRate();
        Optional<CurrencyExchangeRate> existingCurrencyExchangeRate = currencyExchangeRateRepository
                .findByCurrencyCode(currencyCode);
        if (existingCurrencyExchangeRate.isPresent()) {
            CurrencyExchangeRate existing = existingCurrencyExchangeRate.get();
            existing.setExchangeRate(exchangeRate);
            currencyExchangeRateRepository.save(existing);
        } else {
            log.info("saving new currency exchange rate");
            currencyExchangeRateRepository.save(currencyExchangeRate);
        }
    }

    @Override
    @Transactional
    public List<CurrencyExchangeRate> findAll() {
        log.info("retrieving all currency exchange rates");
        return currencyExchangeRateRepository.findAllNotDeleted();
    }

    @Override
    @Transactional
    public List<CurrencyExchangeRateDto> findAllRates() {
        log.info("retrieving all currency exchange rates");
        List<CurrencyExchangeRate> exchangeRates = currencyExchangeRateRepository.findAllNotDeleted();
        return Optional.ofNullable(exchangeRates)
                .orElse(Collections.emptyList())
                .stream()
                .map(currencyExchangeRateDtoMapper::mapEntityToDto)
                .toList();
    }

    @Override
    @Transactional
    public CurrencyExchangeRate findById(Integer id) {
        return currencyExchangeRateRepository.findById(id)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(id)));
    }

    @Override
    @Transactional
    public CurrencyExchangeRate findByCurrencyCode(String currencyCode) {
        log.info("retrieving currency exchange rate by currency code {}", currencyCode);
        return currencyExchangeRateRepository.findByCurrencyCode(currencyCode)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new DataNotFoundException(currencyCode));
    }

    @Override
    @Transactional
    public void update(Integer id, CurrencyExchangeRate currencyExchangeRate) {
        log.info("updating currency exchange rate");
        CurrencyExchangeRate rate = currencyExchangeRateRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(id)));

        if (!rate.isDeleted()) {
            rate.setCurrencyCode(currencyExchangeRate.getCurrencyCode());
            rate.setExchangeRate(currencyExchangeRate.getExchangeRate());
            currencyExchangeRateRepository.save(rate);
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("deleting currency exchange rate");
        CurrencyExchangeRate rate = currencyExchangeRateRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(id)));
        rate.setDeleted(true);
        currencyExchangeRateRepository.save(rate);
    }
}
