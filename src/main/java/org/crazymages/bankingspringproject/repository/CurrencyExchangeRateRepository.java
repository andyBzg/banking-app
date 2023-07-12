package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CurrencyExchangeRateRepository extends JpaRepository <CurrencyExchangeRate, String> {

    @Query("SELECT cer FROM CurrencyExchangeRate cer WHERE cer.isDeleted = false ")
    List<CurrencyExchangeRate> findAllNotDeleted();
}
