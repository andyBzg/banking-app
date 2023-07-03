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
import org.crazymages.bankingspringproject.service.utils.validator.ListValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ListValidator<Product> listValidator;

    /**
     * Creates a new Product entity and saves it to the database.
     * If the product's manager UUID is not set, it assigns the first active manager with the highest product quantity.
     *
     * @param product The Product entity to create.
     */
    @Override
    @Transactional
    public void create(Product product) {
        if (product.getManagerUuid() == null) {
            List<Manager> activeManagers = managerDatabaseService.findManagersSortedByProductQuantityWhereManagerStatusIs(ManagerStatus.ACTIVE);
            Manager firstManager = managerDatabaseService.getFirstManager(activeManagers);
            product.setManagerUuid(firstManager.getUuid());
        }
        productRepository.save(product);
        log.info("product created");
    }

    /**
     * Retrieves a list of all Product entities from the database.
     *
     * @return A list of all Product entities.
     */
    @Override
    public List<Product> findAll() {
        log.info("retrieving list of products");
        List<Product> products = productRepository.findAll();
        return listValidator.validate(products);
    }

    /**
     * Retrieves a list of all not deleted Product entities from the database.
     *
     * @return A list of all not deleted Product entities.
     */
    @Override
    @Transactional
    public List<Product> findAllNotDeleted() {
        log.info("retrieving list of not deleted products");
        List<Product> products = productRepository.findAllNotDeleted();
        return listValidator.validate(products);
    }

    /**
     * Retrieves a list of all deleted Product entities from the database.
     *
     * @return A list of all deleted Product entities.
     */
    @Override
    @Transactional
    public List<Product> findDeletedProducts() {
        log.info("retrieving list of deleted products");
        List<Product> deletedProducts = productRepository.findAllDeleted();
        return listValidator.validate(deletedProducts);
    }

    /**
     * Retrieves a Product entity from the database by its UUID.
     *
     * @param uuid The UUID of the Product entity to retrieve.
     * @return The Product entity with the specified UUID.
     * @throws DataNotFoundException if no Product entity is found with the specified UUID.
     */
    @Override
    public Product findById(UUID uuid) {
        log.info("retrieving product by id {}", uuid);
        return productRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    /**
     * Retrieves a Product entity from the database by its type, status, and currency code.
     *
     * @param type         The type of the Product entity to retrieve.
     * @param status       The status of the Product entity to retrieve.
     * @param currencyCode The currency code of the Product entity to retrieve.
     * @return The Product entity with the specified type, status, and currency code.
     * @throws DataNotFoundException if no Product entity is found with the specified type, status, and currency code.
     */
    @Override
    public Product findProductByTypeAndStatusAndCurrencyCode(ProductType type, ProductStatus status, CurrencyCode currencyCode) {
        log.info("retrieving product by type {}", type);
        return productRepository
                .findProductByTypeIsAndStatusIsAndCurrencyCodeIs(type, ProductStatus.ACTIVE, currencyCode)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(type)));
    }

    /**
     * Updates a Product entity in the database with the provided UUID.
     *
     * @param uuid          The UUID of the Product entity to update.
     * @param productUpdate The updated Product entity.
     * @throws DataNotFoundException if no Product entity is found with the specified UUID.
     */
    @Override
    @Transactional
    public void update(UUID uuid, Product productUpdate) {
        Product product = productRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        product = productUpdateService.update(product, productUpdate);
        productRepository.save(product);
        log.info("updated product id {}", uuid);
    }

    /**
     * Deletes a Product entity from the database with the provided UUID.
     *
     * @param uuid The UUID of the Product entity to delete.
     * @throws DataNotFoundException if no Product entity is found with the specified UUID.
     */
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

