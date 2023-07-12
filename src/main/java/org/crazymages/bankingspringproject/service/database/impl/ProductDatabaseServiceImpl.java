package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.ProductDTO;
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
import org.crazymages.bankingspringproject.service.utils.mapper.impl.ProductDTOMapper;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    private final ProductDTOMapper productDTOMapper;


    @Override
    @Transactional
    @CacheEvict(value = {"productsList", "productsCache"}, allEntries = true)
    public void create(ProductDTO productDTO) {
        Product product = productDTOMapper.mapDtoToEntity(productDTO);
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
    public List<ProductDTO> findAll() {
        log.info("retrieving list of products");
        List<Product> products = productRepository.findAll();
        return productDTOMapper.getListOfDTOs(products);
    }

    @Override
    @Transactional
    @Cacheable(value = "productsList")
    public List<ProductDTO> findAllNotDeleted() {
        log.info("retrieving list of not deleted products");
        List<Product> products = productRepository.findAllNotDeleted();
        return productDTOMapper.getListOfDTOs(products);
    }

    @Override
    @Transactional
    @Cacheable(value = "deletedProducts")
    public List<ProductDTO> findDeletedProducts() {
        log.info("retrieving list of deleted products");
        List<Product> deletedProducts = productRepository.findAllDeleted();
        return productDTOMapper.getListOfDTOs(deletedProducts);
    }

    @Override
    @Transactional
    @Cacheable(value = "productsCache", key = "#uuid")
    public ProductDTO findById(UUID uuid) {
        log.info("retrieving product by id {}", uuid);
        return productDTOMapper.mapEntityToDto(
                productRepository.findById(uuid)
                        .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
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
    public void update(UUID uuid, ProductDTO productDTOUpdate) {
        Product productUpdate = productDTOMapper.mapDtoToEntity(productDTOUpdate);
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
}

