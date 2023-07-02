package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT pr FROM Product pr " +
            "JOIN Agreement ag ON ag.productUuid = pr.uuid " +
            "GROUP BY pr " +
            "HAVING COUNT(ag) > :count")
    List<Product> findAllProductsWhereAgreementsQuantityMoreThan(@Param("count") Integer count);

    @Query("SELECT pr FROM Product pr WHERE pr.updatedAt > pr.createdAt")
    List<Product> findAllChangedProducts();

    @Query("SELECT pr FROM Product pr WHERE pr.isDeleted = false")
    List<Product> findAllNotDeleted();

    @Query("SELECT pr FROM Product pr WHERE pr.isDeleted = true")
    List<Product> findAllDeleted();

    Optional<Product> findProductByTypeIsAndStatusIsAndCurrencyCodeIs(
            ProductType type, ProductStatus status, CurrencyCode currencyCode);

}
