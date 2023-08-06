package org.crazymages.bankingspringproject.service.utils.updater.impl;

import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductUpdateServiceImplTest {

    ProductUpdateServiceImpl productUpdateService;
    Product product;
    Product productUpdate;

    @BeforeEach
    void setUp() {
        productUpdateService = new ProductUpdateServiceImpl();

        product = new Product();
        product.setManagerUuid(UUID.randomUUID());
        product.setName("Test Product");
        product.setStatus(ProductStatus.ACTIVE);
        product.setCurrencyCode(CurrencyCode.EUR);
        product.setInterestRate(BigDecimal.valueOf(0.05));
        product.setLimitation(BigDecimal.valueOf(1000));

        productUpdate = new Product();
        productUpdate.setName("Updated Product");
        productUpdate.setStatus(ProductStatus.UNAVAILABLE);
        productUpdate.setCurrencyCode(CurrencyCode.USD);
        productUpdate.setInterestRate(BigDecimal.valueOf(0.1));
        productUpdate.setLimitation(BigDecimal.valueOf(2000));
    }

    @Test
    void update_withValidFields_updatesProductProperties() {
        // when
        Product actual = productUpdateService.update(product, productUpdate);

        // then
        assertEquals(productUpdate.getName(), actual.getName());
        assertEquals(productUpdate.getStatus(), actual.getStatus());
        assertEquals(productUpdate.getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(productUpdate.getInterestRate(), actual.getInterestRate());
        assertEquals(productUpdate.getLimitation(), actual.getLimitation());
    }

    @Test
    void update_withNullProduct_doesNotUpdateProduct_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productUpdateService.update(null, productUpdate));
    }

    @Test
    void update_withNullProductUpdate_doesNotUpdateProduct_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productUpdateService.update(productUpdate, null));
    }

    @Test
    void updateProperties_productUpdateWithNullFields_updatesOnlyExistentProperties() {
        // given
        Product productUpdate = new Product();
        productUpdate.setName("Updated Product");
        productUpdate.setStatus(ProductStatus.UNAVAILABLE);
        productUpdate.setCurrencyCode(null);
        productUpdate.setInterestRate(null);
        productUpdate.setLimitation(null);

        // when
        Product actual = productUpdateService.updateProperties(product, productUpdate);

        // then
        assertEquals(productUpdate.getName(), actual.getName());
        assertEquals(productUpdate.getStatus(), actual.getStatus());
        assertEquals(product.getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(product.getInterestRate(), actual.getInterestRate());
        assertEquals(product.getLimitation(), actual.getLimitation());
    }

    @Test
    void updateProperties_productWithNullProperties_returnsProductWithUpdatedProperties() {
        // given
        Product product = new Product();

        // when
        Product actual = productUpdateService.updateProperties(product, productUpdate);

        // then
        assertEquals(productUpdate.getName(), actual.getName());
        assertEquals(productUpdate.getStatus(), actual.getStatus());
        assertEquals(productUpdate.getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(productUpdate.getInterestRate(), actual.getInterestRate());
        assertEquals(productUpdate.getLimitation(), actual.getLimitation());
    }

    @Test
    void updateProperties_updateWithNullProperties_doesNotUpdateProductProperties() {
        // given
        Product productUpdate1 = new Product();

        // when
        Product actual = productUpdateService.updateProperties(product, productUpdate1);

        // then
        assertEquals(product.getName(), actual.getName());
        assertEquals(product.getStatus(), actual.getStatus());
        assertEquals(product.getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(product.getInterestRate(), actual.getInterestRate());
        assertEquals(product.getLimitation(), actual.getLimitation());
    }
}
