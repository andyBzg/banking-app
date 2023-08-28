package org.crazymages.bankingspringproject.dto.agreement;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data transfer object (DTO) class representing an Agreement.
 */
@Data
@Builder
public class AgreementDto {
    private String accountUuid;
    private String productUuid;
    private BigDecimal interestRate;
    private String status;
    private BigDecimal amount;
}
