package org.crazymages.bankingspringproject.dto.mapper.product;

import org.crazymages.bankingspringproject.dto.ProductDto;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Component class that provides mapping functionality between Product and ProductDTO objects.
 */
@Component
public class ProductDtoMapper implements DtoMapper<Product, ProductDto> {

    @Override
    public ProductDto mapEntityToDto(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product cannot be null");
        }
        return ProductDto.builder()
                .managerUuid(product.getManagerUuid() != null ? product.getManagerUuid().toString() : null)
                .name(product.getName())
                .status(product.getStatus() != null ? product.getStatus().name() : null)
                .type(product.getType() != null ? product.getType().name() : null)
                .currencyCode(product.getCurrencyCode() != null ? product.getCurrencyCode().name() : null)
                .interestRate(product.getInterestRate())
                .limitation(product.getLimitation())
                .build();
    }

    @Override
    public Product mapDtoToEntity(ProductDto productDto) {
        if (productDto == null) {
            throw new IllegalArgumentException("productDto cannot be null");
        }
        return Product.builder()
                .managerUuid(productDto.getManagerUuid() != null ? UUID.fromString(productDto.getManagerUuid()) : null)
                .name(productDto.getName())
                .status(productDto.getStatus() != null ? ProductStatus.valueOf(productDto.getStatus()) : null)
                .type(productDto.getType() != null ? ProductType.valueOf(productDto.getType()) : null)
                .currencyCode(productDto.getCurrencyCode() != null ? CurrencyCode.valueOf(productDto.getCurrencyCode()) : null)
                .interestRate(productDto.getInterestRate())
                .limitation(productDto.getLimitation())
                .build();
    }

    @Override
    public List<ProductDto> getDtoList(List<Product> productList) {
        return Optional.ofNullable(productList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
