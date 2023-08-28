package org.crazymages.bankingspringproject.service.utils.matcher;

import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.service.utils.matcher.impl.ProductTypeMatcherImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductTypeMatcherTest {

    ProductTypeMatcherImpl productTypeMatcher;

    @BeforeEach
    void setUp() {
        productTypeMatcher = new ProductTypeMatcherImpl();
    }

    @Test
    void matchTypes_withCurrentAccountType_returnsCurrentProductType() {
        // given
        AccountType accountType = AccountType.CURRENT;

        // when
        ProductType actual = productTypeMatcher.matchTypes(accountType);

        // then
        assertEquals(ProductType.CURRENT_ACCOUNT, actual);
    }

    @Test
    void matchTypes_withSavingsAccountType_returnsSavingsProductType() {
        // given
        AccountType accountType = AccountType.SAVINGS;

        // when
        ProductType actual = productTypeMatcher.matchTypes(accountType);

        // then
        assertEquals(ProductType.SAVINGS_ACCOUNT, actual);
    }
}
