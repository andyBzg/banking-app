package org.crazymages.bankingspringproject.dto.transaction;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data transfer object (DTO) class representing a Transaction.
 */
@Data
@Builder
public class TransactionDto {
    private String debitAccountUuid;
    private String creditAccountUuid;
    private String type;
    private String currencyCode;
    private BigDecimal amount;
    private String description;
}
