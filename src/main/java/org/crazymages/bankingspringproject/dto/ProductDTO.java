package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data transfer object (DTO) class representing a Product.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID uuid;
    private UUID managerUuid;
    private String name;
    private ProductStatus status;
    private ProductType type;
    private CurrencyCode currencyCode;
    private BigDecimal interestRate;
    private BigDecimal limitation;
}
