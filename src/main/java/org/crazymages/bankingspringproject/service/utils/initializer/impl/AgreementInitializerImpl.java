package org.crazymages.bankingspringproject.service.utils.initializer.impl;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.crazymages.bankingspringproject.service.utils.initializer.AgreementInitializer;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * A component class responsible for initializing new Agreement entities based on the provided account UUID and Product.
 */
@Component
public class AgreementInitializerImpl implements AgreementInitializer {
    @Override
    public Agreement initializeAgreement(UUID accountUuid, Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product is null");
        }
        Agreement agreement = new Agreement();
        agreement.setAccountUuid(accountUuid);
        agreement.setProductUuid(product.getUuid());
        agreement.setInterestRate(product.getInterestRate());
        agreement.setStatus(AgreementStatus.PENDING);
        agreement.setAmount(BigDecimal.ZERO);
        return agreement;
    }
}
