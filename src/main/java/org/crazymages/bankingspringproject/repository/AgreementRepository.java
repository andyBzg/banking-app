package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, UUID> {

    @Query("SELECT ag FROM Agreement ag " +
            "JOIN Product pr ON pr.uuid = ag.productUuid " +
            "JOIN Manager mg ON mg.uuid = pr.managerUuid " +
            "WHERE mg.uuid = :managerUuid")
    List<Agreement> findAgreementsWhereManagerIdIs(@Param("managerUuid") UUID managerUuid);

    @Query("SELECT ag FROM Agreement ag " +
            "JOIN Account ac ON ac.uuid = ag.accountUuid " +
            "JOIN Client cl ON cl.uuid = ac.clientUuid " +
            "WHERE cl.uuid = :clientUuid")
    List<Agreement> findAgreementsWhereClientIdIs(@Param("clientUuid") UUID clientUuid);

    @Query("SELECT ag FROM Agreement ag " +
            "JOIN Account ac ON ac.uuid = ag.accountUuid " +
            "JOIN Client cl ON cl.uuid = ac.clientUuid " +
            "JOIN Product pr ON pr.uuid = ag.productUuid " +
            "WHERE cl.uuid = :clientUuid " +
            "AND pr.type = :type")
    Optional<Agreement> findAgreementByClientIdAndProductType(@Param("clientUuid") UUID clientUuid, @Param("type")ProductType type);

//    @Query("SELECT ag FROM Agreement ag " +
//            "JOIN Product pr ON pr.uuid = ag.productUuid " +
//            "WHERE ag.status = :status " +
//            "AND pr.type = :type")
//    List<Agreement> findAgreementsWhereStatusIsAndProductTypeIs(@Param("status")AgreementStatus status, @Param("type")ProductType type);

//    Agreement findAgreementWhereProductTypeIs(UUID uuid);
}
