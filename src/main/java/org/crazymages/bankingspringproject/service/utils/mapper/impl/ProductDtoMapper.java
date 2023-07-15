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
        ProductDto productDto = new ProductDto();
        productDto.setUuid(String.valueOf(product.getUuid()));
        productDto.setManagerUuid(String.valueOf(product.getManagerUuid()));
        productDto.setName(product.getName());
        productDto.setStatus(String.valueOf(product.getStatus()));
        productDto.setType(String.valueOf(product.getType()));
        productDto.setCurrencyCode(String.valueOf(product.getCurrencyCode()));
        productDto.setInterestRate(product.getInterestRate());
        productDto.setLimitation(product.getLimitation());
        return productDto;
    }

    @Override
    public Product mapDtoToEntity(ProductDto productDto) {
        Product product = new Product();
        product.setUuid(UUID.fromString(productDto.getUuid()));
        product.setManagerUuid(UUID.fromString(productDto.getManagerUuid()));
        product.setName(productDto.getName());
        product.setStatus(ProductStatus.valueOf(productDto.getStatus()));
        product.setType(ProductType.valueOf(productDto.getType()));
        product.setCurrencyCode(CurrencyCode.valueOf(productDto.getCurrencyCode()));
        product.setInterestRate(productDto.getInterestRate());
        product.setLimitation(productDto.getLimitation());
        return product;
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
