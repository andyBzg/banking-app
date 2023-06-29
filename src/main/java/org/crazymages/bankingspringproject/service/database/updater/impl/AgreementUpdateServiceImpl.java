package org.crazymages.bankingspringproject.service.database.updater.impl;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.service.database.updater.EntityUpdateService;
import org.springframework.stereotype.Service;

@Service
public class AgreementUpdateServiceImpl implements EntityUpdateService<Agreement> {
    @Override
    public Agreement update(Agreement agreement, Agreement agreementUpdate) {
        if (agreement != null && agreementUpdate != null) {
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
        }
        return agreement;
    }
}
