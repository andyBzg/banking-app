package org.crazymages.bankingspringproject.dto.mapper.product;

import org.crazymages.bankingspringproject.dto.ProductDto;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductDtoMapperTest {

    ProductDtoMapper productDtoMapper;
    ProductDto productDto;
    Product product1;
    Product product2;

    @BeforeEach
    void setUp() {
        productDtoMapper = new ProductDtoMapper();
        productDto = ProductDto.builder().build();

        product1 = new Product();
        product1.setUuid(UUID.fromString("30348dce-45f7-4e19-aa08-3ed77a8f7ac3"));
        product1.setManagerUuid(UUID.randomUUID());
        product1.setName("Product 1");
        product1.setStatus(ProductStatus.ACTIVE);
        product1.setType(ProductType.CURRENT_ACCOUNT);
        product1.setCurrencyCode(CurrencyCode.EUR);
        product1.setInterestRate(BigDecimal.valueOf(0.05));
        product1.setLimitation(BigDecimal.valueOf(1000));

        product2 = new Product();
        product2.setUuid(UUID.randomUUID());
        product2.setManagerUuid(UUID.randomUUID());
        product2.setName("Product 2");
        product2.setStatus(ProductStatus.CLOSED);
        product2.setType(ProductType.SAVINGS_ACCOUNT);
        product2.setCurrencyCode(CurrencyCode.USD);
        product2.setInterestRate(BigDecimal.valueOf(0.03));
        product2.setLimitation(BigDecimal.valueOf(500));
    }

    @Test
    void mapEntityToDto_validProduct_success() {
        // when
        ProductDto productDto = productDtoMapper.mapEntityToDto(product1);

        // then
        assertEquals(product1.getManagerUuid().toString(), productDto.getManagerUuid());
        assertEquals(product1.getName(), productDto.getName());
        assertEquals(product1.getStatus().toString(), productDto.getStatus());
        assertEquals(product1.getType().toString(), productDto.getType());
        assertEquals(product1.getCurrencyCode().toString(), productDto.getCurrencyCode());
        assertEquals(product1.getInterestRate(), productDto.getInterestRate());
        assertEquals(product1.getLimitation(), productDto.getLimitation());
    }

    @Test
    void mapEntityToDto_missingProductProperties_returnsProductDtoWithNullProperties() {
        // given
        Product product = new Product();

        // when
        ProductDto productDto = productDtoMapper.mapEntityToDto(product);

        // then
        assertNull(productDto.getManagerUuid());
        assertNull(productDto.getName());
        assertNull(productDto.getStatus());
        assertNull(productDto.getType());
        assertNull(productDto.getCurrencyCode());
        assertNull(productDto.getInterestRate());
        assertNull(productDto.getLimitation());
    }

    @Test
    void mapEntityToDto_nullProduct_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validProductDto_success() {
        // given
        productDto.setManagerUuid("f59f83b7-9f9b-495b-83e7-09c11856e6a5");
        productDto.setName("Product 1");
        productDto.setStatus("ACTIVE");
        productDto.setType("CURRENT_ACCOUNT");
        productDto.setCurrencyCode("EUR");
        productDto.setInterestRate(BigDecimal.valueOf(0.05));
        productDto.setLimitation(BigDecimal.valueOf(1000));

        // when
        Product product = productDtoMapper.mapDtoToEntity(productDto);

        // then
        assertFalse(product.isDeleted());
        assertEquals(UUID.fromString(productDto.getManagerUuid()), product.getManagerUuid());
        assertEquals(productDto.getName(), product.getName());
        assertEquals(ProductStatus.valueOf(productDto.getStatus()), product.getStatus());
        assertEquals(ProductType.valueOf(productDto.getType()), product.getType());
        assertEquals(CurrencyCode.valueOf(productDto.getCurrencyCode()), product.getCurrencyCode());
        assertEquals(productDto.getInterestRate(), product.getInterestRate());
        assertEquals(productDto.getLimitation(), product.getLimitation());
    }

    @Test
    void mapDtoToEntity_nullProductDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingProductDtoProperties_returnsProductWithNullProperties() {
        // given
        ProductDto productDto = ProductDto.builder().build();

        // when
        Product product = productDtoMapper.mapDtoToEntity(productDto);

        // then
        assertFalse(product.isDeleted());
        assertNull(product.getManagerUuid());
        assertNull(product.getName());
        assertNull(product.getStatus());
        assertNull(product.getType());
        assertNull(product.getCurrencyCode());
        assertNull(product.getInterestRate());
        assertNull(product.getLimitation());
    }
}
