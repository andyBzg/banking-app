package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.ProductDto;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DtoMapper;
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
                .managerUuid(String.valueOf(product.getManagerUuid()))
                .name(product.getName())
                .status(String.valueOf(product.getStatus()))
                .type(String.valueOf(product.getType()))
                .currencyCode(String.valueOf(product.getCurrencyCode()))
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
                .managerUuid(UUID.fromString(productDto.getManagerUuid()))
                .name(productDto.getName())
                .status(ProductStatus.valueOf(productDto.getStatus()))
                .type(ProductType.valueOf(productDto.getType()))
                .currencyCode(CurrencyCode.valueOf(productDto.getCurrencyCode()))
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
