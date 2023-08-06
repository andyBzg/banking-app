package org.crazymages.bankingspringproject.service.utils.converter.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.crazymages.bankingspringproject.service.utils.converter.CurrencyConverter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A component class responsible for performing currency conversion for a given amount between two accounts.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyConverterImpl implements CurrencyConverter {

    private final CurrencyExchangeRateDatabaseService currencyExchangeRateDatabaseService;

    @Override
    public Account performCurrencyConversion(BigDecimal amount, Account recipientAccount, Account senderAccount) {
        String recipientCurrencyCode = recipientAccount.getCurrencyCode().name();
        CurrencyExchangeRate recipientToBaseCurrencyRate = currencyExchangeRateDatabaseService
                .findByCurrencyCode(recipientCurrencyCode);
        BigDecimal recipientCurrencyRate = recipientToBaseCurrencyRate.getExchangeRate();
        log.info("Курс валюты получателя к доллару {}", recipientCurrencyRate);

        String senderCurrencyCode = senderAccount.getCurrencyCode().name();
        CurrencyExchangeRate senderToBaseCurrencyRate = currencyExchangeRateDatabaseService
                .findByCurrencyCode(senderCurrencyCode);
        BigDecimal senderCurrencyRate = senderToBaseCurrencyRate.getExchangeRate();
        log.info("Курс валюты отправителя к доллару {}", senderCurrencyRate);

        BigDecimal baseCurrencyAmount = amount.divide(senderCurrencyRate, 2, RoundingMode.HALF_UP);
        log.info("сумма в долларах: {}", baseCurrencyAmount);

        BigDecimal recipientAmount = baseCurrencyAmount.multiply(recipientCurrencyRate);
        log.info("сумма в валюте получателя: {}", recipientAmount);

        BigDecimal recipientBalance = recipientAccount.getBalance();
        recipientAccount.setBalance(recipientBalance.add(recipientAmount));
        return recipientAccount;
    }
}
