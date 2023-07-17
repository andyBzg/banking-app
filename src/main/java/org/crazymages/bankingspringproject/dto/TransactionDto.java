package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data transfer object (DTO) class representing a Transaction.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String uuid;
    private String debitAccountUuid;
    private String creditAccountUuid;
    private String type;
    private String currencyCode;
    private BigDecimal amount;
    private String description;
}
