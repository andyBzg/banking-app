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

/**
 * The repository interface for managing products.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /**
     * Finds all products where the quantity of agreements is more than the specified count.
     *
     * @param count The minimum count of agreements
     * @return The list of products
     */
    @Query("SELECT pr FROM Product pr " +
            "JOIN Agreement ag ON ag.productUuid = pr.uuid " +
            "GROUP BY pr " +
            "HAVING COUNT(ag) > :count")
    List<Product> findAllProductsWhereAgreementsQuantityMoreThan(@Param("count") Integer count);

    /**
     * Finds all changed products (products where the updated timestamp is greater than the created timestamp).
     *
     * @return The list of changed products
     */
    @Query("SELECT pr FROM Product pr WHERE pr.updatedAt > pr.createdAt")
    List<Product> findAllChangedProducts();

    /**
     * Finds all products that are not deleted.
     *
     * @return The list of products that are not deleted
     */
    @Query("SELECT pr FROM Product pr WHERE pr.isDeleted = false")
    List<Product> findAllNotDeleted();

    /**
     * Finds all deleted products.
     *
     * @return The list of deleted products
     */
    @Query("SELECT pr FROM Product pr WHERE pr.isDeleted = true")
    List<Product> findAllDeleted();

    /**
     * Finds a product by type, status, and currency code.
     *
     * @param type         The product type
     * @param status       The product status
     * @param currencyCode The currency code
     * @return The optional product
     */
    Optional<Product> findProductByTypeIsAndStatusIsAndCurrencyCodeIs(
            ProductType type, ProductStatus status, CurrencyCode currencyCode);
}
