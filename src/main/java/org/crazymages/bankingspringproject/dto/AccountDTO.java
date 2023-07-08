package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data transfer object (DTO) class representing an Account.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private UUID uuid;
    private UUID clientUuid;
    private String name;
    private AccountType type;
    private AccountStatus status;
    private BigDecimal balance;
    private CurrencyCode currencyCode;
}
