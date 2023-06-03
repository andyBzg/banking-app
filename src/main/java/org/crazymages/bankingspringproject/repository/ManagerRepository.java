package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {
}
