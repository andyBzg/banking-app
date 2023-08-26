package org.crazymages.bankingspringproject.service.utils.matcher.impl;

import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.service.utils.matcher.ProductTypeMatcher;
import org.springframework.stereotype.Component;

/**
 * Implementation of the {@link ProductTypeMatcher} interface.
 * Matches an {@link AccountType} value to its corresponding {@link ProductType} value.
 */
@Component
public class ProductTypeMatcherImpl implements ProductTypeMatcher {
    @Override
    public ProductType matchTypes(AccountType value) {
        return ProductType.values()[value.ordinal()];
    }
}
