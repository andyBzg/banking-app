package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The repository interface for managing agreements.
 */
@Repository
public interface AgreementRepository extends JpaRepository<Agreement, UUID> {

    /**
     * Finds agreements where manager ID matches.
     *
     * @param managerUuid The manager UUID
     * @return The list of agreements belonging to the manager
     */
    @Query("SELECT ag FROM Agreement ag " +
            "JOIN Product pr ON pr.uuid = ag.productUuid " +
            "WHERE pr.managerUuid = :managerUuid")
    List<Agreement> findAgreementsWhereManagerIdIs(@Param("managerUuid") UUID managerUuid);

    /**
     * Finds agreements where client ID matches.
     *
     * @param clientUuid The client UUID
     * @return The list of agreements belonging to the client
     */
    @Query("SELECT ag FROM Agreement ag " +
            "JOIN Account ac ON ac.uuid = ag.accountUuid " +
            "WHERE ac.clientUuid = :clientUuid")
    List<Agreement> findAgreementsWhereClientIdIs(@Param("clientUuid") UUID clientUuid);

    /**
     * Finds an agreement by client ID and product type.
     *
     * @param clientUuid The client UUID
     * @param type       The product type
     * @return The optional agreement matching the given client UUID and product type
     */
    @Query("SELECT ag FROM Agreement ag " +
            "JOIN Account ac ON ac.uuid = ag.accountUuid " +
            "JOIN Client cl ON cl.uuid = ac.clientUuid " +
            "JOIN Product pr ON pr.uuid = ag.productUuid " +
            "WHERE cl.uuid = :clientUuid " +
            "AND pr.type = :type")
    Optional<Agreement> findAgreementByClientIdAndProductType(
            @Param("clientUuid") UUID clientUuid, @Param("type") ProductType type);

    /**
     * Finds all agreements that are not deleted.
     *
     * @return The list of agreements that are not deleted
     */
    @Query("SELECT ag FROM Agreement ag WHERE ag.isDeleted = false")
    List<Agreement> findAllNotDeleted();

    /**
     * Finds all deleted agreements.
     *
     * @return The list of deleted agreements
     */
    @Query("SELECT ag FROM Agreement ag WHERE ag.isDeleted = true")
    List<Agreement> findAllDeleted();
}

