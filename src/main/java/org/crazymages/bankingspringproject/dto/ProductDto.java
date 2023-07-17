package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data transfer object (DTO) class representing a Product.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private String uuid;
    private String managerUuid;
    private String name;
    private String status;
    private String type;
    private String currencyCode;
    private BigDecimal interestRate;
    private BigDecimal limitation;
}
