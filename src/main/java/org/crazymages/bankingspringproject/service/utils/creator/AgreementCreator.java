package org.crazymages.bankingspringproject.service.utils.creator;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiFunction;

/**
 * A component class that implements the BiFunction interface to create an Agreement object
 * based on the provided account UUID and product information.
 */
@Component
public class AgreementCreator implements BiFunction<UUID, Product, Agreement> {

    @Override
    public Agreement apply(UUID accountUuid, Product product) {
        Agreement agreement = new Agreement();
        agreement.setAccountUuid(accountUuid);
        agreement.setProductUuid(product.getUuid());
        agreement.setInterestRate(product.getInterestRate());
        agreement.setStatus(AgreementStatus.PENDING);
        return agreement;
    }
}
