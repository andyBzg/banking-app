package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Integer> {
}
