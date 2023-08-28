package org.crazymages.bankingspringproject.dto.account;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data transfer object (DTO) class representing an Account.
 */
@Data
@Builder
public class AccountDto {
    private String clientUuid;
    private String name;
    private String type;
    private String status;
    private BigDecimal balance;
    private String currencyCode;
}
