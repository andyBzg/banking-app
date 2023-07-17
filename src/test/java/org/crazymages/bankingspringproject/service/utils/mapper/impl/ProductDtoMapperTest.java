package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.ProductDto;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
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
        productDto = new ProductDto();

        product1 = new Product();
        product1.setUuid(UUID.randomUUID());
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
        assertEquals(product1.getUuid().toString(), productDto.getUuid());
        assertEquals(product1.getManagerUuid().toString(), productDto.getManagerUuid());
        assertEquals(product1.getName(), productDto.getName());
        assertEquals(product1.getStatus().toString(), productDto.getStatus());
        assertEquals(product1.getType().toString(), productDto.getType());
        assertEquals(product1.getCurrencyCode().toString(), productDto.getCurrencyCode());
        assertEquals(product1.getInterestRate(), productDto.getInterestRate());
        assertEquals(product1.getLimitation(), productDto.getLimitation());
    }

    @Test
    void mapEntityToDto_nullProduct_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> productDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validProductDto_success() {
        // given
        productDto.setUuid("30348dce-45f7-4e19-aa08-3ed77a8f7ac3");
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
        assertEquals(UUID.fromString(productDto.getUuid()), product.getUuid());
        assertEquals(UUID.fromString(productDto.getManagerUuid()), product.getManagerUuid());
        assertEquals(productDto.getName(), product.getName());
        assertEquals(ProductStatus.valueOf(productDto.getStatus()), product.getStatus());
        assertEquals(ProductType.valueOf(productDto.getType()), product.getType());
        assertEquals(CurrencyCode.valueOf(productDto.getCurrencyCode()), product.getCurrencyCode());
        assertEquals(productDto.getInterestRate(), product.getInterestRate());
        assertEquals(productDto.getLimitation(), product.getLimitation());
    }

    @Test
    void mapDtoToEntity_nullProductDto_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> productDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingManagerDtoProperties_throwsIllegalArgumentException() {
        // given
        productDto.setUuid("30348dce-45f7-4e19-aa08-3ed77a8f7ac3");
        productDto.setManagerUuid("f59f83b7-9f9b-495b-83e7-09c11856e6a5");

        // when, then
        assertThrows(NullPointerException.class, () -> productDtoMapper.mapDtoToEntity(productDto));
    }

    @Test
    void getDtoList_validProductList_success() {
        // given
        List<Product> productList = List.of(product1, product2);

        // when
        List<ProductDto> actual = productDtoMapper.getDtoList(productList);

        // then
        assertEquals(2, actual.size());

        ProductDto productDto1 = actual.get(0);
        assertEquals(product1.getUuid().toString(), productDto1.getUuid());
        assertEquals(product1.getManagerUuid().toString(), productDto1.getManagerUuid());
        assertEquals(product1.getName(), productDto1.getName());
        assertEquals(product1.getStatus().toString(), productDto1.getStatus());
        assertEquals(product1.getType().toString(), productDto1.getType());
        assertEquals(product1.getCurrencyCode().toString(), productDto1.getCurrencyCode());
        assertEquals(product1.getInterestRate(), productDto1.getInterestRate());
        assertEquals(product1.getLimitation(), productDto1.getLimitation());

        ProductDto productDto2 = actual.get(1);
        assertEquals(product2.getUuid().toString(), productDto2.getUuid());
        assertEquals(product2.getManagerUuid().toString(), productDto2.getManagerUuid());
        assertEquals(product2.getName(), productDto2.getName());
        assertEquals(product2.getStatus().toString(), productDto2.getStatus());
        assertEquals(product2.getType().toString(), productDto2.getType());
        assertEquals(product2.getCurrencyCode().toString(), productDto2.getCurrencyCode());
        assertEquals(product2.getInterestRate(), productDto2.getInterestRate());
        assertEquals(product2.getLimitation(), productDto2.getLimitation());
    }

    @Test
    void getDtoList_nullProductList_throwsDataNotFoundException() {
        assertThrows(DataNotFoundException.class, () -> productDtoMapper.getDtoList(null));
    }

    @Test
    void getDtoList_emptyProductList_returnsEmptyList() {
        // given
        List<Product> productList = Collections.emptyList();

        // when
        List<ProductDto> actual = productDtoMapper.getDtoList(productList);

        // then
        assertTrue(actual.isEmpty());
    }
}
