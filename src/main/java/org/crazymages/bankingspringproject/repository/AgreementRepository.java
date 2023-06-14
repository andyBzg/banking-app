package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, UUID> {

    //TODO findAgreementsWhereManagerIDIs(managerId: String): List<Agreement>

    //TODO findAgreementsWhereClientIdIs(clientId: String): List<Agreement>

}
