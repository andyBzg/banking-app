package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.ProductDTO;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Product and ProductDTO objects.
 */
@Component
public class ProductDTOMapper implements DTOMapper<Product, ProductDTO> {

    @Override
    public ProductDTO mapEntityToDto(Product product) {
        return new ProductDTO(
                product.getUuid(),
                product.getManagerUuid(),
                product.getName(),
                product.getStatus(),
                product.getType(),
                product.getCurrencyCode(),
                product.getInterestRate(),
                product.getLimitation()
        );
    }

    @Override
    public Product mapDtoToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setUuid(productDTO.getUuid());
        product.setManagerUuid(productDTO.getManagerUuid());
        product.setName(productDTO.getName());
        product.setStatus(productDTO.getStatus());
        product.setType(productDTO.getType());
        product.setCurrencyCode(productDTO.getCurrencyCode());
        product.setInterestRate(productDTO.getInterestRate());
        product.setLimitation(productDTO.getLimitation());
        return product;
    }

    @Override
    public List<ProductDTO> getListOfDTOs(List<Product> productList) {
        return Optional.ofNullable(productList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
