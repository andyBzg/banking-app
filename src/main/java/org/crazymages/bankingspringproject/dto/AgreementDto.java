package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data transfer object (DTO) class representing an Agreement.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementDto {
    private String uuid;
    private String accountUuid;
    private String productUuid;
    private BigDecimal interestRate;
    private String status;
    private BigDecimal amount;
}
