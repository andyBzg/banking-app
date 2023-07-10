package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data transfer object (DTO) class representing an Agreement.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementDTO {
    private UUID uuid;
    private UUID accountUuid;
    private UUID productUuid;
    private BigDecimal interestRate;
    private AgreementStatus status;
    private BigDecimal amount;
}
