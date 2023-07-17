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
    }

    @Test
    void update_withValidFields_updatesProductProperties() {
        // given
        Product productUpdate = new Product();
        productUpdate.setName("Updated Product");
        productUpdate.setStatus(ProductStatus.UNAVAILABLE);
        productUpdate.setCurrencyCode(CurrencyCode.USD);
        productUpdate.setInterestRate(BigDecimal.valueOf(0.1));
        productUpdate.setLimitation(BigDecimal.valueOf(2000));

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
    void updateProperties_productUpdateWithNullFields_updatesOnlyExistentProperties() {
        // given
        Product productUpdate = new Product();
        productUpdate.setName(null);
        productUpdate.setStatus(null);
        productUpdate.setCurrencyCode(CurrencyCode.USD);
        productUpdate.setInterestRate(BigDecimal.valueOf(0.1));
        productUpdate.setLimitation(null);

        // when
        Product actual = productUpdateService.updateProperties(product, productUpdate);

        // then
        assertEquals(product.getName(), actual.getName());
        assertEquals(product.getStatus(), actual.getStatus());
        assertEquals(productUpdate.getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(productUpdate.getInterestRate(), actual.getInterestRate());
        assertEquals(product.getLimitation(), actual.getLimitation());
    }

    @Test
    void update_withNullProduct_doesNotUpdateProductProperties_returnsNull() {
        // given
        Product productUpdate = new Product();
        productUpdate.setName("Updated Product");
        productUpdate.setStatus(ProductStatus.UNAVAILABLE);

        // when
        Product actual = productUpdateService.update(null, productUpdate);

        // then
        assertNull(actual);
    }

    @Test
    void update_withNullProductUpdate_doesNotUpdateProductProperties() {
        // given
        Product productUpdate = null;

        // when
        Product actual = productUpdateService.update(product, productUpdate);

        // then
        assertEquals(product.getName(), actual.getName());
        assertEquals(product.getStatus(), actual.getStatus());
        assertEquals(product.getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(product.getInterestRate(), actual.getInterestRate());
        assertEquals(product.getLimitation(), actual.getLimitation());
    }

    @Test
    void updateProperties_withNullProductUpdate_throwsNullPointerException() {
        // given
        Product productUpdate = null;

        // when, then
        assertThrows(NullPointerException.class, () -> productUpdateService.updateProperties(product, productUpdate));
    }
}
