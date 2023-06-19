package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {

    @Query("SELECT mg FROM Manager mg " +
            "JOIN Product pr ON pr.managerUuid = mg.uuid " +
            "GROUP BY mg " +
            "ORDER BY COUNT(pr) DESC")
    List<Manager> findAllManagersSortedByProductQuantity();

}
