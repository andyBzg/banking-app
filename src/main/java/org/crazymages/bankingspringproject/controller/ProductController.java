package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.dto.ProductDto;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing products.
 */
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductDatabaseService productDatabaseService;

    /**
     * Creates a new product.
     *
     * @param productDto The product to create.
     * @return The created product.
     */
    @PostMapping(value = "/product/create")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        productDatabaseService.create(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    /**
     * Retrieves all products.
     *
     * @return The list of products.
     */
    @GetMapping(value = "/product/find/all")
    public ResponseEntity<List<ProductDto>> findAllProducts() {
        List<ProductDto> productDtoList = productDatabaseService.findAllNotDeleted();
        return productDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productDtoList);
    }

    /**
     * Retrieves a product by its UUID.
     *
     * @param uuid The UUID of the product.
     * @return The product.
     */
    @GetMapping(value = "/product/find/{uuid}")
    public ResponseEntity<ProductDto> findProductByUuid(@PathVariable String uuid) {
        ProductDto productDto = productDatabaseService.findById(uuid);
        return ResponseEntity.ok(productDto);
    }

    /**
     * Updates an existing product.
     *
     * @param uuid              The UUID of the product to update.
     * @param updatedProductDto The updated product.
     * @return The updated product.
     */
    @PutMapping(value = "/product/update/{uuid}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable String uuid, @RequestBody ProductDto updatedProductDto) {
        productDatabaseService.update(uuid, updatedProductDto);
        return ResponseEntity.ok(updatedProductDto);
    }

    /**
     * Deletes a product by its UUID.
     *
     * @param uuid The UUID of the product to delete.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/product/delete/{uuid}")
    public ResponseEntity<String> deleteProduct(@PathVariable String uuid) {
        productDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
