package org.crazymages.bankingspringproject.service.utils.converter.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.service.database.CurrencyExchangeRateDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConverterImplTest {

    @Mock
    CurrencyExchangeRateDatabaseService currencyExchangeRateDatabaseService;

    @InjectMocks
    CurrencyConverterImpl currencyConverter;

    @BeforeEach
    void setUp() {
    }

    @Test
    void performCurrencyConversion_success() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);

        String recipientCurrencyCode = "EUR";
        CurrencyExchangeRate recipientToBaseCurrencyRate = new CurrencyExchangeRate();
        recipientToBaseCurrencyRate.setCurrencyCode(recipientCurrencyCode);
        recipientToBaseCurrencyRate.setExchangeRate(BigDecimal.valueOf(0.89));

        Account recipientAccount = new Account();
        recipientAccount.setBalance(BigDecimal.ZERO);
        recipientAccount.setCurrencyCode(CurrencyCode.EUR);

        when(currencyExchangeRateDatabaseService
                .findByCurrencyCode(recipientCurrencyCode)).thenReturn(recipientToBaseCurrencyRate);

        String senderCurrencyCode = "AUD";
        CurrencyExchangeRate senderToBaseCurrencyRate = new CurrencyExchangeRate();
        senderToBaseCurrencyRate.setCurrencyCode(senderCurrencyCode);
        senderToBaseCurrencyRate.setExchangeRate(BigDecimal.valueOf(1.46));

        Account senderAccount = new Account();
        senderAccount.setCurrencyCode(CurrencyCode.AUD);
        senderAccount.setBalance(BigDecimal.valueOf(200));

        when(currencyExchangeRateDatabaseService
                .findByCurrencyCode(senderCurrencyCode)).thenReturn(senderToBaseCurrencyRate);

        BigDecimal baseCurrencyAmount = amount.divide(senderToBaseCurrencyRate.getExchangeRate(), 2, RoundingMode.HALF_UP);
        BigDecimal recipientAmount = baseCurrencyAmount.multiply(recipientToBaseCurrencyRate.getExchangeRate());
        BigDecimal recipientBalance = recipientAccount.getBalance();
        Account expected = new Account();
        expected.setBalance(recipientBalance.add(recipientAmount));


        // when
        Account actual = currencyConverter.performCurrencyConversion(amount, recipientAccount, senderAccount);
        System.out.println(actual.getBalance());


        //then
        verify(currencyExchangeRateDatabaseService).findByCurrencyCode(recipientCurrencyCode);
        verify(currencyExchangeRateDatabaseService).findByCurrencyCode(senderCurrencyCode);
        assertEquals(expected.getBalance(), actual.getBalance());
    }

    @Test
    void performCurrencyConversion_withZEROAmount_returnsZERO() {
        // given
        BigDecimal amount = BigDecimal.ZERO;

        String recipientCurrencyCode = "EUR";
        CurrencyExchangeRate recipientToBaseCurrencyRate = new CurrencyExchangeRate();
        recipientToBaseCurrencyRate.setCurrencyCode(recipientCurrencyCode);
        recipientToBaseCurrencyRate.setExchangeRate(BigDecimal.valueOf(0.89));

        Account recipientAccount = new Account();
        recipientAccount.setBalance(BigDecimal.ZERO);
        recipientAccount.setCurrencyCode(CurrencyCode.EUR);

        when(currencyExchangeRateDatabaseService
                .findByCurrencyCode(recipientCurrencyCode)).thenReturn(recipientToBaseCurrencyRate);

        String senderCurrencyCode = "AUD";
        CurrencyExchangeRate senderToBaseCurrencyRate = new CurrencyExchangeRate();
        senderToBaseCurrencyRate.setCurrencyCode(senderCurrencyCode);
        senderToBaseCurrencyRate.setExchangeRate(BigDecimal.valueOf(1.46));

        Account senderAccount = new Account();
        senderAccount.setCurrencyCode(CurrencyCode.AUD);
        senderAccount.setBalance(BigDecimal.valueOf(200));

        when(currencyExchangeRateDatabaseService
                .findByCurrencyCode(senderCurrencyCode)).thenReturn(senderToBaseCurrencyRate);

        BigDecimal baseCurrencyAmount = amount.divide(senderToBaseCurrencyRate.getExchangeRate(), 2, RoundingMode.HALF_UP);
        BigDecimal recipientAmount = baseCurrencyAmount.multiply(recipientToBaseCurrencyRate.getExchangeRate());
        BigDecimal recipientBalance = recipientAccount.getBalance();
        Account expected = new Account();
        expected.setBalance(recipientBalance.add(recipientAmount));


        // when
        Account actual = currencyConverter.performCurrencyConversion(amount, recipientAccount, senderAccount);
        System.out.println(actual.getBalance());


        //then
        verify(currencyExchangeRateDatabaseService).findByCurrencyCode(recipientCurrencyCode);
        verify(currencyExchangeRateDatabaseService).findByCurrencyCode(senderCurrencyCode);
        assertEquals(expected.getBalance(), actual.getBalance());
    }


    @Test
    void performCurrencyConversion_withNegativeAmount_settsNegativeBalance() {
        // given
        BigDecimal amount = BigDecimal.valueOf(-100);

        String recipientCurrencyCode = "EUR";
        CurrencyExchangeRate recipientToBaseCurrencyRate = new CurrencyExchangeRate();
        recipientToBaseCurrencyRate.setCurrencyCode(recipientCurrencyCode);
        recipientToBaseCurrencyRate.setExchangeRate(BigDecimal.valueOf(0.89));

        Account recipientAccount = new Account();
        recipientAccount.setBalance(BigDecimal.ZERO);
        recipientAccount.setCurrencyCode(CurrencyCode.EUR);

        when(currencyExchangeRateDatabaseService
                .findByCurrencyCode(recipientCurrencyCode)).thenReturn(recipientToBaseCurrencyRate);

        String senderCurrencyCode = "AUD";
        CurrencyExchangeRate senderToBaseCurrencyRate = new CurrencyExchangeRate();
        senderToBaseCurrencyRate.setCurrencyCode(senderCurrencyCode);
        senderToBaseCurrencyRate.setExchangeRate(BigDecimal.valueOf(1.46));

        Account senderAccount = new Account();
        senderAccount.setCurrencyCode(CurrencyCode.AUD);
        senderAccount.setBalance(BigDecimal.valueOf(200));

        when(currencyExchangeRateDatabaseService
                .findByCurrencyCode(senderCurrencyCode)).thenReturn(senderToBaseCurrencyRate);

        BigDecimal baseCurrencyAmount = amount.divide(senderToBaseCurrencyRate.getExchangeRate(), 2, RoundingMode.HALF_UP);
        BigDecimal recipientAmount = baseCurrencyAmount.multiply(recipientToBaseCurrencyRate.getExchangeRate());
        BigDecimal recipientBalance = recipientAccount.getBalance();
        Account expected = new Account();
        expected.setBalance(recipientBalance.add(recipientAmount));


        // when
        Account actual = currencyConverter.performCurrencyConversion(amount, recipientAccount, senderAccount);
        System.out.println(actual.getBalance());


        //then
        verify(currencyExchangeRateDatabaseService).findByCurrencyCode(recipientCurrencyCode);
        verify(currencyExchangeRateDatabaseService).findByCurrencyCode(senderCurrencyCode);
        assertEquals(expected.getBalance(), actual.getBalance());
    }

    @Test
    void performCurrencyConversion_withNullRecipientAccount_throwsIllegalArgumentException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        Account senderAccount = new Account();
        senderAccount.setCurrencyCode(CurrencyCode.AUD);
        senderAccount.setBalance(BigDecimal.valueOf(200));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> currencyConverter
                .performCurrencyConversion(amount, null, senderAccount));
    }

    @Test
    void performCurrencyConversion_withNullSenderAccount_throwsIllegalArgumentException() {
        // given
        BigDecimal amount = BigDecimal.valueOf(100);
        Account recipientAccount = new Account();
        recipientAccount.setCurrencyCode(CurrencyCode.EUR);
        recipientAccount.setBalance(BigDecimal.ZERO);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> currencyConverter
                .performCurrencyConversion(amount, recipientAccount, null));
    }

    @Test
    void performCurrencyConversion_withNullAmount_throwsIllegalArgumentException() {
        // given
        Account recipientAccount = new Account();
        recipientAccount.setCurrencyCode(CurrencyCode.EUR);
        recipientAccount.setBalance(BigDecimal.ZERO);
        Account senderAccount = new Account();
        senderAccount.setCurrencyCode(CurrencyCode.AUD);
        senderAccount.setBalance(BigDecimal.valueOf(200));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> currencyConverter
                .performCurrencyConversion(null, recipientAccount, senderAccount));
    }
}
