package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ProductRepository;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDatabaseServiceImpl implements ProductDatabaseService {

    private final ProductRepository productRepository;

    @Override
    public void create(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(UUID uuid) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        return productOptional.orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public Product update(UUID uuid, Product productUpdate) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setManager(productUpdate.getManager());
            product.setName(productUpdate.getName());
            product.setStatus(productUpdate.getStatus());
            product.setCurrencyCode(productUpdate.getCurrencyCode());
            product.setInterestRate(productUpdate.getInterestRate());
            product.setLimitation(productUpdate.getLimitation());
            productRepository.save(product);
        }
        return productOptional.orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.deleteById(uuid);
    }
}
