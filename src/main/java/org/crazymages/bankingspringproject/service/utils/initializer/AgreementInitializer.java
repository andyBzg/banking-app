package org.crazymages.bankingspringproject.service.utils.initializer;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Product;

import java.util.UUID;

public interface AgreementInitializer {
    /**
     * Initializes a new Agreement entity based on the provided account UUID and Product.
     *
     * @param accountUuid The UUID of the account associated with the Agreement.
     * @param product     The Product associated with the Agreement.
     * @return The initialized Agreement entity.
     */
    Agreement initializeAgreement(UUID accountUuid, Product product);
}
