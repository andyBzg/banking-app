package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data transfer object (DTO) class representing a Transaction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private UUID uuid;
    private UUID debitAccountUuid;
    private UUID creditAccountUuid;
    private TransactionType type;
    private CurrencyCode currencyCode;
    private BigDecimal amount;
    private String description;
}
