package org.crazymages.bankingspringproject.service.utils.creator;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * A component class responsible for initializing new Agreement entities based on the provided account UUID and Product.
 */
@Component
public class AgreementInitializer {

    /**
     * Initializes a new Agreement entity based on the provided account UUID and Product.
     *
     * @param accountUuid The UUID of the account associated with the Agreement.
     * @param product     The Product associated with the Agreement.
     * @return The initialized Agreement entity.
     * @throws IllegalArgumentException if the provided product is null.
     */
    public Agreement initializeAgreement(UUID accountUuid, Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product is null");
        }
        Agreement agreement = new Agreement();
        agreement.setAccountUuid(accountUuid);
        agreement.setProductUuid(product.getUuid());
        agreement.setInterestRate(product.getInterestRate());
        agreement.setStatus(AgreementStatus.PENDING);
        return agreement;
    }
}
