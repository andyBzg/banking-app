package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ProductRepository;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.crazymages.bankingspringproject.service.database.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDatabaseServiceImpl implements ProductDatabaseService {

    private final ProductRepository productRepository;
    private final EntityUpdateService<Product> productUpdateService;

    @Override
    public void create(Product product) {
        productRepository.save(product);
        log.info("product created");
    }

    @Override
    public List<Product> findAll() {
        log.info("retrieving list of products");
        return productRepository.findAll();
    }

    @Override
    public Product findById(UUID uuid) {
        log.info("retrieving product by id {}", uuid);
        return productRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public void update(UUID uuid, Product productUpdate) {
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        product = productUpdateService.update(product, productUpdate);
        productRepository.save(product);
        log.info("updated product id {}", uuid);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        product.setDeleted(true);
        productRepository.save(product);
        log.info("deleted product id {}", uuid);
    }
}
