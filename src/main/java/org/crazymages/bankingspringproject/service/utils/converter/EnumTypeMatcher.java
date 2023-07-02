package org.crazymages.bankingspringproject.service.utils.converter;

import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.springframework.stereotype.Component;

@Component
public class EnumTypeMatcher {

    public ProductType matchTypes(AccountType value) {
        return ProductType.values()[value.ordinal()];
    }
}
