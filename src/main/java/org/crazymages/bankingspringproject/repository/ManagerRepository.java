package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * The repository interface for managing managers.
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {

    /**
     * Finds all managers sorted by product quantity, where the manager status is the specified status.
     *
     * @param status The manager status
     * @return The list of managers sorted by product quantity
     */
    @Query("SELECT mg FROM Manager mg " +
            "JOIN Product pr ON pr.managerUuid = mg.uuid " +
            "WHERE mg.status = :status " +
            "GROUP BY mg " +
            "ORDER BY COUNT(pr) ASC")
    List<Manager> findAllManagersSortedByProductQuantityWhereManagerStatusIs(@Param("status") ManagerStatus status);

    /**
     * Finds managers sorted by client count, where the manager status is the specified status.
     *
     * @param status The manager status
     * @return The list of managers sorted by client count
     */
    @Query("SELECT mg FROM Manager mg " +
            "JOIN Client cl ON cl.managerUuid = mg.uuid " +
            "WHERE mg.status = :status " +
            "GROUP BY mg " +
            "ORDER BY COUNT(cl) ASC")
    List<Manager> findManagersSortedByClientCountWhereManagerStatusIs(@Param("status") ManagerStatus status);

    /**
     * Finds all managers that are not deleted.
     *
     * @return The list of managers that are not deleted
     */
    @Query("SELECT mg FROM Manager mg WHERE mg.isDeleted = false")
    List<Manager> findAllNotDeleted();

    /**
     * Finds all deleted managers.
     *
     * @return The list of deleted managers
     */
    @Query("SELECT mg FROM Manager mg WHERE mg.isDeleted = true")
    List<Manager> findAllDeleted();
}
