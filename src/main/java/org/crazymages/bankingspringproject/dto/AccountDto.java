package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data transfer object (DTO) class representing an Account.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String uuid;
    private String clientUuid;
    private String name;
    private String type;
    private String status;
    private BigDecimal balance;
    private String currencyCode;
}
