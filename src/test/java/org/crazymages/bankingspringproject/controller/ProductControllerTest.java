package org.crazymages.bankingspringproject.controller;

import org.crazymages.bankingspringproject.dto.ProductDto;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductDatabaseService productDatabaseService;

    @InjectMocks
    ProductController productController;

    String uuid;

    @BeforeEach
    void setUp() {
        uuid = "7bcf30be-8c6e-4e10-a73b-706849fc94dc";
    }

    @Test
    void createProduct_success() {
        // when
        ProductDto productDto = ProductDto.builder().build();
        ProductDto createdProductDto = ProductDto.builder().build();

        // when
        ResponseEntity<ProductDto> actual = productController.createProduct(productDto);

        // then
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(createdProductDto, actual.getBody());
        verify(productDatabaseService).create(productDto);
    }

    @Test
    void createProduct_emptyProductDto_savesNoData() {
        // when
        ResponseEntity<ProductDto> actual = productController.createProduct(null);

        // then
        assertNull(actual.getBody());
        verify(productDatabaseService, never()).create(any(ProductDto.class));
    }

    @Test
    void findAllProducts_success() {
        // given
        List<ProductDto> expected = List.of(ProductDto.builder().build(), ProductDto.builder().build());
        when(productDatabaseService.findAllNotDeleted()).thenReturn(expected);

        // when
        ResponseEntity<List<ProductDto>> actual = productController.findAllProducts();

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(productDatabaseService).findAllNotDeleted();
    }

    @Test
    void findAllProducts_withEmptyList_returnsNoContentStatus() {
        // given
        List<ProductDto> expected = Collections.emptyList();
        when(productDatabaseService.findAllNotDeleted()).thenReturn(expected);

        // when
        ResponseEntity<List<ProductDto>> actual = productController.findAllProducts();

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(productDatabaseService).findAllNotDeleted();
    }

    @Test
    void findProductByUuid_success() {
        // given
        ProductDto expected = ProductDto.builder().build();
        when(productDatabaseService.findById(uuid)).thenReturn(expected);

        // when
        ResponseEntity<ProductDto> actual = productController.findProductByUuid(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(productDatabaseService).findById(uuid);
    }

    @Test
    void updateProduct_success() {
        // given
        ProductDto expected = ProductDto.builder().build();

        // when
        ResponseEntity<ProductDto> actual = productController.updateProduct(uuid, expected);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(productDatabaseService).update(uuid, expected);
    }

    @Test
    void deleteProduct_success() {
        // when
        ResponseEntity<String> actual = productController.deleteProduct(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        verify(productDatabaseService).delete(uuid);
    }
}
