package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductDatabaseService {

    void create(Product product);

    List<Product> findAll();

    Product findById(UUID uuid);

    void update(UUID uuid, Product product);

    void delete(UUID uuid);
}
