package org.crazymages.bankingspringproject.service.utils.mapper;

import org.crazymages.bankingspringproject.dto.ProductDTO;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Product and ProductDTO objects.
 */
@Component
public class ProductDTOMapper {

    /**
     * Maps a Product object to a ProductDTO object.
     *
     * @param product The Product object to be mapped.
     * @return The mapped ProductDTO object.
     */
    public ProductDTO mapToProductDTO(Product product) {
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

    /**
     * Maps an ProductDTO object to a Product object.
     *
     * @param productDTO The ProductDTO object to be mapped.
     * @return The mapped Product object.
     */
    public Product mapToProduct(ProductDTO productDTO) {
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

    /**
     * Maps a list of Product objects to a list of ProductDTO objects.
     *
     * @param productList The list of Product objects to be mapped.
     * @return The list of mapped ProductDTO objects.
     * @throws DataNotFoundException If the input productList is null.
     */
    public List<ProductDTO> getListOfProductDTOs(List<Product> productList) {
        return Optional.ofNullable(productList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapToProductDTO)
                .toList();
    }
}
