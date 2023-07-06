package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ProductRepository;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A service implementation for managing Product entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDatabaseServiceImpl implements ProductDatabaseService {

    private final ProductRepository productRepository;
    private final EntityUpdateService<Product> productUpdateService;
    private final ManagerDatabaseService managerDatabaseService;


    @Override
    @Transactional
    @CacheEvict(value = {"productsList", "productsCache"}, allEntries = true)
    public void create(Product product) {
        if (product.getManagerUuid() == null) {
            List<Manager> activeManagers = managerDatabaseService
                    .findManagersSortedByProductQuantityWhereManagerStatusIs(ManagerStatus.ACTIVE);
            Manager firstManager = managerDatabaseService.getFirstManager(activeManagers);
            product.setManagerUuid(firstManager.getUuid());
        }
        productRepository.save(product);
        log.info("product created");
    }

    @Override
    @Transactional
    public List<Product> findAll() {
        log.info("retrieving list of products");
        List<Product> products = productRepository.findAll();
        return checkListForNull(products);
    }

    @Override
    @Transactional
    @Cacheable(value = "productsList")
    public List<Product> findAllNotDeleted() {
        log.info("retrieving list of not deleted products");
        List<Product> products = productRepository.findAllNotDeleted();
        return checkListForNull(products);
    }

    @Override
    @Transactional
    @Cacheable(value = "deletedProducts")
    public List<Product> findDeletedProducts() {
        log.info("retrieving list of deleted products");
        List<Product> deletedProducts = productRepository.findAllDeleted();
        return checkListForNull(deletedProducts);
    }

    @Override
    @Transactional
    @Cacheable(value = "productsCache", key = "#uuid")
    public Product findById(UUID uuid) {
        log.info("retrieving product by id {}", uuid);
        return productRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public Product findProductByTypeAndStatusAndCurrencyCode(ProductType type, ProductStatus status, CurrencyCode currencyCode) {
        log.info("retrieving product by type {}", type);
        return productRepository
                .findProductByTypeIsAndStatusIsAndCurrencyCodeIs(type, ProductStatus.ACTIVE, currencyCode)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(type)));
    }

    @Override
    @Transactional
    @CachePut(value = "productsCache", key = "#uuid")
    @CacheEvict(value = "productsList", allEntries = true)
    public void update(UUID uuid, Product productUpdate) {
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        product = productUpdateService.update(product, productUpdate);
        productRepository.save(product);
        log.info("updated product id {}", uuid);
    }

    @Override
    @Transactional
    @CachePut(value = "deletedProducts", key = "#uuid")
    @CacheEvict(value = {"productsList", "productsCache"}, allEntries = true)
    public void delete(UUID uuid) {
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        product.setDeleted(true);
        productRepository.save(product);
        log.info("deleted product id {}", uuid);
    }

    private List<Product> checkListForNull(List<Product> list) {
        return list == null ? Collections.emptyList() : list;
    }
}

