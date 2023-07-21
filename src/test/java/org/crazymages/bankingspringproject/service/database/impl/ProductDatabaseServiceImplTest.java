package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.ProductDto;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ProductRepository;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.ProductDtoMapper;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDatabaseServiceImplTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    EntityUpdateService<Product> productUpdateService;
    @Mock
    ManagerDatabaseService managerDatabaseService;
    @Mock
    ProductDtoMapper productDTOMapper;

    @InjectMocks
    ProductDatabaseServiceImpl productDatabaseService;

    Product product1;
    Product product2;
    ProductDto productDto1;
    ProductDto productDto2;
    UUID uuid;
    List<Product> products;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product2 = new Product();
        productDto1 = ProductDto.builder().build();
        productDto2 = ProductDto.builder().build();
        uuid = UUID.fromString("d358838e-1134-4101-85ac-5d99e8debfae");
        products = List.of(product1, product2);
    }

    @Test
    void create_productWithoutManagerUuid_success() {
        // given
        ManagerStatus status = ManagerStatus.ACTIVE;
        List<Manager> managers = List.of(new Manager(), new Manager());
        Manager firstManager = new Manager();
        firstManager.setUuid(uuid);
        when(productDTOMapper.mapDtoToEntity(productDto1)).thenReturn(product1);
        when(managerDatabaseService.findManagersSortedByProductQuantityWhereManagerStatusIs(status))
                .thenReturn(managers);
        when(managerDatabaseService.getFirstManager(managers)).thenReturn(firstManager);

        // when
        productDatabaseService.create(productDto1);

        // then
        assertEquals(product1.getManagerUuid(), firstManager.getUuid());
        verify(productDTOMapper).mapDtoToEntity(productDto1);
        verify(managerDatabaseService).findManagersSortedByProductQuantityWhereManagerStatusIs(status);
        verify(managerDatabaseService).getFirstManager(managers);
        verify(productRepository).save(product1);
    }

    @Test
    void create_productHasManagerUuid_success() {
        // given
        UUID managerUuid = uuid;
        product1.setManagerUuid(managerUuid);
        productDto1.setManagerUuid(String.valueOf(managerUuid));
        when(productDTOMapper.mapDtoToEntity(productDto1)).thenReturn(product1);

        // when
        productDatabaseService.create(productDto1);

        // then
        assertEquals(managerUuid, product1.getManagerUuid());
        verify(productDTOMapper).mapDtoToEntity(productDto1);
        verify(productRepository).save(product1);
        verify(managerDatabaseService, times(0))
                .findManagersSortedByProductQuantityWhereManagerStatusIs(any(ManagerStatus.class));
        verify(managerDatabaseService, times(0)).getFirstManager(anyList());
    }

    @Test
    void findAll_success() {
        // given
        List<ProductDto> expected = List.of(productDto1, productDto2);
        when(productRepository.findAll()).thenReturn(products);
        when(productDTOMapper.getDtoList(products)).thenReturn(expected);

        // when
        List<ProductDto> actual = productDatabaseService.findAll();

        // then
        assertEquals(expected, actual);
        verify(productRepository).findAll();
        verify(productDTOMapper).getDtoList(products);
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        List<ProductDto> expected = List.of(productDto1, productDto2);
        when(productRepository.findAllNotDeleted()).thenReturn(products);
        when(productDTOMapper.getDtoList(products)).thenReturn(expected);

        // when
        List<ProductDto> actual = productDatabaseService.findAllNotDeleted();

        // then
        assertEquals(expected, actual);
        verify(productRepository).findAllNotDeleted();
        verify(productDTOMapper).getDtoList(products);
    }

    @Test
    void findDeletedProducts_success() {
        // given
        List<ProductDto> expected = List.of(productDto1, productDto2);
        when(productRepository.findAllDeleted()).thenReturn(products);
        when(productDTOMapper.getDtoList(products)).thenReturn(expected);

        // when
        List<ProductDto> actual = productDatabaseService.findDeletedProducts();

        // then
        assertEquals(expected, actual);
        verify(productRepository).findAllDeleted();
        verify(productDTOMapper).getDtoList(products);
    }

    @Test
    void findById_existingProduct_success() {
        // given
        ProductDto expected = productDto1;
        when(productRepository.findById(uuid)).thenReturn(Optional.ofNullable(product1));
        when(productDTOMapper.mapEntityToDto(product1)).thenReturn(productDto1);

        // when
        ProductDto actual = productDatabaseService.findById(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(productRepository).findById(uuid);
        verify(productDTOMapper).mapEntityToDto(product1);
    }

    @Test
    void findById_nonExistingProduct_throwsDataNotFoundException() {
        // given
        String strUuid = "d358838e-1134-4101-85ac-5d99e8debfae";
        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> productDatabaseService.findById(strUuid));

        // then
        verify(productRepository).findById(uuid);
        verify(productDTOMapper, times(0)).mapEntityToDto(any(Product.class));
    }

    @Test
    void findProductByTypeAndStatusAndCurrencyCode_success() {
        // given
        ProductType type = ProductType.DEPOSIT_ACCOUNT;
        ProductStatus status = ProductStatus.ACTIVE;
        CurrencyCode currencyCode = CurrencyCode.EUR;
        Product expected = product1;
        when(productRepository
                .findProductByTypeIsAndStatusIsAndCurrencyCodeIs(type, status, currencyCode))
                .thenReturn(Optional.ofNullable(product1));

        // when
        Product actual = productDatabaseService.findProductByTypeAndStatusAndCurrencyCode(type, status, currencyCode);

        // then
        assertEquals(expected, actual);
        verify(productRepository).findProductByTypeIsAndStatusIsAndCurrencyCodeIs(type, status, currencyCode);
    }

    @Test
    void update_success() {
        // given
        ProductDto productDtoUpdate = productDto1;
        Product productUpdate = product1;
        Product product = product2;

        when(productDTOMapper.mapDtoToEntity(productDtoUpdate)).thenReturn(productUpdate);
        when(productRepository.findById(uuid)).thenReturn(Optional.ofNullable(product));
        when(productUpdateService.update(product, productUpdate)).thenReturn(product1);

        // when
        productDatabaseService.update(String.valueOf(uuid), productDtoUpdate);

        // then
        verify(productDTOMapper).mapDtoToEntity(productDtoUpdate);
        verify(productRepository).findById(uuid);
        verify(productUpdateService).update(product, productUpdate);
        verify(productRepository).save(product1);
    }

    @Test
    void update_nonExistentProduct_throwsDataNotFoundException() {
        // given
        String strUuid = "d358838e-1134-4101-85ac-5d99e8debfae";
        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> productDatabaseService.update(strUuid, productDto1));

        // then
        verify(productDTOMapper).mapDtoToEntity(productDto1);
        verify(productUpdateService, times(0)).update(any(Product.class), any(Product.class));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void delete_success() {
        // given
        when(productRepository.findById(uuid)).thenReturn(Optional.ofNullable(product1));

        // when
        productDatabaseService.delete(String.valueOf(uuid));

        // then
        assertTrue(product1.isDeleted());
        verify(productRepository).findById(uuid);
        verify(productRepository).save(product1);
    }

    @Test
    void delete_nonExistentProduct_throwsDataNotFoundException() {
        // given
        String strUuid = "d358838e-1134-4101-85ac-5d99e8debfae";
        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> productDatabaseService.delete(strUuid));

        // then
        verify(productRepository).findById(uuid);
        verify(productRepository, times(0)).save(any(Product.class));
    }
}
