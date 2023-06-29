package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Agreement;

import java.util.List;
import java.util.UUID;

public interface AgreementDatabaseService {

    void create(Agreement agreement);

    List<Agreement> findAll();

    Agreement findById(UUID uuid);

    Agreement findSavingsAgreementByClientId(UUID uuid);

    void update(UUID uuid, Agreement agreement);

    void delete(UUID uuid);

    List<Agreement> findAgreementsByManagerUuid(UUID uuid);

    List<Agreement> findAgreementsByClientUuid(UUID uuid);

//    List<Agreement> findRecurringPaymentAgreements();
}
