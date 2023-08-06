package org.crazymages.bankingspringproject.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data transfer object (DTO) class representing a Product.
 */
@Data
@Builder
public class ProductDto {
    private String managerUuid;
    private String name;
    private String status;
    private String type;
    private String currencyCode;
    private BigDecimal interestRate;
    private BigDecimal limitation;
}
