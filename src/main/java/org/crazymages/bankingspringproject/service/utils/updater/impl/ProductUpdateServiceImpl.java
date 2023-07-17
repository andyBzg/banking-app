package org.crazymages.bankingspringproject.service.utils.updater.impl;

import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Component;

/**
 * A class implementing the EntityUpdateService interface for updating Product entities.
 * It provides custom update logic for the Product entity.
 */
@Component
public class ProductUpdateServiceImpl implements EntityUpdateService<Product> {

    @Override
    public Product update(Product product, Product productUpdate) {
        if (product != null && productUpdate != null) {
            product = updateProperties(product, productUpdate);
        }
        return product;
    }

    @Override
    public Product updateProperties(Product product, Product productUpdate) {
        if (productUpdate.getManagerUuid() != null) {
            product.setManagerUuid(productUpdate.getManagerUuid());
        }
        if (productUpdate.getName() != null) {
            product.setName(productUpdate.getName());
        }
        if (productUpdate.getStatus() != null) {
            product.setStatus(productUpdate.getStatus());
        }
        if (productUpdate.getCurrencyCode() != null) {
            product.setCurrencyCode(productUpdate.getCurrencyCode());
        }
        if (productUpdate.getInterestRate() != null) {
            product.setInterestRate(productUpdate.getInterestRate());
        }
        if (productUpdate.getLimitation() != null) {
            product.setLimitation(productUpdate.getLimitation());
        }
        return product;
    }
}
