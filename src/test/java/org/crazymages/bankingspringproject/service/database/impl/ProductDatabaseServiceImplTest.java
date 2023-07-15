package org.crazymages.bankingspringproject.service.database.impl;

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
import org.crazymages.bankingspringproject.service.utils.mapper.impl.ProductDTOMapper;
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
    ProductDTOMapper productDTOMapper;

    @InjectMocks
    ProductDatabaseServiceImpl productDatabaseService;

    Product product1;
    Product product2;
    ProductDTO productDTO1;
    ProductDTO productDTO2;
    UUID uuid;
    List<Product> products;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product2 = new Product();
        productDTO1 = new ProductDTO();
        productDTO2 = new ProductDTO();
        uuid = UUID.randomUUID();
        products = List.of(product1, product2);
    }

    @Test
    void create_productWithoutManagerUuid_success() {
        // given
        ManagerStatus status = ManagerStatus.ACTIVE;
        List<Manager> managers = List.of(new Manager(), new Manager());
        Manager firstManager = new Manager();
        firstManager.setUuid(uuid);
        when(productDTOMapper.mapDtoToEntity(productDTO1)).thenReturn(product1);
        when(managerDatabaseService.findManagersSortedByProductQuantityWhereManagerStatusIs(status))
                .thenReturn(managers);
        when(managerDatabaseService.getFirstManager(managers)).thenReturn(firstManager);

        // when
        productDatabaseService.create(productDTO1);

        // then
        assertEquals(product1.getManagerUuid(), firstManager.getUuid());
        verify(productDTOMapper).mapDtoToEntity(productDTO1);
        verify(managerDatabaseService).findManagersSortedByProductQuantityWhereManagerStatusIs(status);
        verify(managerDatabaseService).getFirstManager(managers);
        verify(productRepository).save(product1);
    }

    @Test
    void create_productHasManagerUuid_success() {
        // given
        UUID managerUuid = uuid;
        product1.setManagerUuid(managerUuid);
        productDTO1.setManagerUuid(managerUuid);
        when(productDTOMapper.mapDtoToEntity(productDTO1)).thenReturn(product1);

        // when
        productDatabaseService.create(productDTO1);

        // then
        assertEquals(managerUuid, product1.getManagerUuid());
        verify(productDTOMapper).mapDtoToEntity(productDTO1);
        verify(productRepository).save(product1);
        verify(managerDatabaseService, times(0))
                .findManagersSortedByProductQuantityWhereManagerStatusIs(any(ManagerStatus.class));
        verify(managerDatabaseService, times(0)).getFirstManager(anyList());
    }

    @Test
    void findAll_success() {
        // given
        List<ProductDTO> expected = List.of(productDTO1, productDTO2);
        when(productRepository.findAll()).thenReturn(products);
        when(productDTOMapper.getListOfDTOs(products)).thenReturn(expected);

        // when
        List<ProductDTO> actual = productDatabaseService.findAll();

        // then
        assertEquals(expected, actual);
        verify(productRepository).findAll();
        verify(productDTOMapper).getListOfDTOs(products);
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        List<ProductDTO> expected = List.of(productDTO1, productDTO2);
        when(productRepository.findAllNotDeleted()).thenReturn(products);
        when(productDTOMapper.getListOfDTOs(products)).thenReturn(expected);

        // when
        List<ProductDTO> actual = productDatabaseService.findAllNotDeleted();

        // then
        assertEquals(expected, actual);
        verify(productRepository).findAllNotDeleted();
        verify(productDTOMapper).getListOfDTOs(products);
    }

    @Test
    void findDeletedProducts_success() {
        // given
        List<ProductDTO> expected = List.of(productDTO1, productDTO2);
        when(productRepository.findAllDeleted()).thenReturn(products);
        when(productDTOMapper.getListOfDTOs(products)).thenReturn(expected);

        // when
        List<ProductDTO> actual = productDatabaseService.findDeletedProducts();

        // then
        assertEquals(expected, actual);
        verify(productRepository).findAllDeleted();
        verify(productDTOMapper).getListOfDTOs(products);
    }

    @Test
    void findById_existingProduct_success() {
        // given
        ProductDTO expected = productDTO1;
        when(productRepository.findById(uuid)).thenReturn(Optional.ofNullable(product1));
        when(productDTOMapper.mapEntityToDto(product1)).thenReturn(productDTO1);

        // when
        ProductDTO actual = productDatabaseService.findById(uuid);

        // then
        assertEquals(expected, actual);
        verify(productRepository).findById(uuid);
        verify(productDTOMapper).mapEntityToDto(product1);
    }

    @Test
    void findById_nonExistingProduct_throwsDataNotFoundException() {
        // given
        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> productDatabaseService.findById(uuid));

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
        ProductDTO productDTOUpdate = productDTO1;
        Product productUpdate = product1;
        Product product = product2;

        when(productDTOMapper.mapDtoToEntity(productDTOUpdate)).thenReturn(productUpdate);
        when(productRepository.findById(uuid)).thenReturn(Optional.ofNullable(product));
        when(productUpdateService.update(product, productUpdate)).thenReturn(product1);

        // when
        productDatabaseService.update(uuid, productDTOUpdate);

        // then
        verify(productDTOMapper).mapDtoToEntity(productDTOUpdate);
        verify(productRepository).findById(uuid);
        verify(productUpdateService).update(product, productUpdate);
        verify(productRepository).save(product1);
    }

    @Test
    void update_nonExistentProduct_throwsDataNotFoundException() {
        // given
        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> productDatabaseService.update(uuid, productDTO1));

        // then
        verify(productDTOMapper).mapDtoToEntity(productDTO1);
        verify(productUpdateService, times(0)).update(any(Product.class), any(Product.class));
        verify(productRepository, times(0)).save(any(Product.class));
    }

    @Test
    void delete_success() {
        // given
        when(productRepository.findById(uuid)).thenReturn(Optional.ofNullable(product1));

        // when
        productDatabaseService.delete(uuid);

        // then
        assertTrue(product1.isDeleted());
        verify(productRepository).findById(uuid);
        verify(productRepository).save(product1);
    }

    @Test
    void delete_nonExistentProduct_throwsDataNotFoundException() {
        // given
        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> productDatabaseService.delete(uuid));

        // then
        verify(productRepository).findById(uuid);
        verify(productRepository, times(0)).save(any(Product.class));
    }
}
