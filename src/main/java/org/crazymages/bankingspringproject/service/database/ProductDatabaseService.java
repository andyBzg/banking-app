package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;

import java.util.List;
import java.util.UUID;

public interface ProductDatabaseService {

    void create(Product product);

    List<Product> findAll();

    List<Product> findAllNotDeleted();

    List<Product> findDeletedProducts();

    Product findById(UUID uuid);

    Product findProductByTypeAndStatusAndCurrencyCode(ProductType type, ProductStatus status, CurrencyCode currencyCode);

    void update(UUID uuid, Product product);

    void delete(UUID uuid);
}
