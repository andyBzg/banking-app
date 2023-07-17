package org.crazymages.bankingspringproject.service.utils.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyConverter {

    private final CurrencyExchangeRateDatabaseService currencyExchangeRateDatabaseService;

    public Account performCurrencyConversion(BigDecimal amount, Account recipientAccount, Account senderAccount) {
        String recipientCurrencyCode = recipientAccount.getCurrencyCode().name();
        CurrencyExchangeRate recipientToBaseCurrencyRate = currencyExchangeRateDatabaseService
                .findById(recipientCurrencyCode);
        BigDecimal recipientCurrencyRate = recipientToBaseCurrencyRate.getExchangeRate();
        log.info("Курс валюты получателя к доллару {}", recipientCurrencyRate);

        String senderCurrencyCode = senderAccount.getCurrencyCode().name();
        CurrencyExchangeRate senderToBaseCurrencyRate = currencyExchangeRateDatabaseService
                .findById(senderCurrencyCode);
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
