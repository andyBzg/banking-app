package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    /*
    findAllProductsWhereAgreementsQuantityMoreThan(quantity: Int): List<Product>
    findAllChangedProducts(): List<Product>
    */
}
