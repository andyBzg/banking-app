package org.crazymages.bankingspringproject.service.utils.updater.impl;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Component;

/**
 * A class implementing the EntityUpdateService interface for updating Agreement entities.
 * It provides custom update logic for the Agreement entity.
 */
@Component
public class AgreementUpdateServiceImpl implements EntityUpdateService<Agreement> {

    @Override
    public Agreement update(Agreement agreement, Agreement agreementUpdate) {
        if (agreement == null || agreementUpdate == null) {
            throw new IllegalArgumentException("argument is null");
        }
        return updateProperties(agreement, agreementUpdate);
    }

    @Override
    public Agreement updateProperties(Agreement agreement, Agreement agreementUpdate) {
        if (agreementUpdate.getAccountUuid() != null) {
            agreement.setAccountUuid(agreementUpdate.getAccountUuid());
        }
        if (agreementUpdate.getProductUuid() != null) {
            agreement.setProductUuid(agreementUpdate.getProductUuid());
        }
        if (agreementUpdate.getInterestRate() != null) {
            agreement.setInterestRate(agreementUpdate.getInterestRate());
        }
        if (agreementUpdate.getStatus() != null) {
            agreement.setStatus(agreementUpdate.getStatus());
        }
        if (agreementUpdate.getAmount() != null) {
            agreement.setAmount(agreementUpdate.getAmount());
        }
        return agreement;
    }
}
