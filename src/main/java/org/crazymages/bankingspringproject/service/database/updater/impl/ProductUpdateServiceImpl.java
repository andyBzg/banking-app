package org.crazymages.bankingspringproject.service.database.updater.impl;

import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.service.database.updater.EntityUpdateService;
import org.springframework.stereotype.Service;

@Service
public class ProductUpdateServiceImpl implements EntityUpdateService<Product> {
    @Override
    public Product update(Product product, Product productUpdate) {
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
