package org.crazymages.bankingspringproject.service.utils.converter;

import org.crazymages.bankingspringproject.entity.Account;

import java.math.BigDecimal;

public interface CurrencyConverter {
    /**
     * Performs currency conversion for the given amount between the sender and recipient accounts.
     *
     * @param amount           The amount to be converted.
     * @param recipientAccount The Account representing the recipient of the converted amount.
     * @param senderAccount    The Account representing the sender of the amount to be converted.
     * @return The updated recipient account with the converted amount added to its balance.
     */
    Account performCurrencyConversion(BigDecimal amount, Account recipientAccount, Account senderAccount);
}
