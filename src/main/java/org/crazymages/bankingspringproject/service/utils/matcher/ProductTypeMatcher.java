package org.crazymages.bankingspringproject.service.utils.matcher;

import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.springframework.stereotype.Component;

/**
 * A class representing an enum type matcher.
 * It matches an account type to a corresponding product type.
 */
@Component
public class ProductTypeMatcher {

    /**
     * Matches an account type to a corresponding product type.
     *
     * @param value The account type to match.
     * @return The product type corresponding to the given account type.
     */
    public ProductType matchTypes(AccountType value) {
        return ProductType.values()[value.ordinal()];
    }
}
