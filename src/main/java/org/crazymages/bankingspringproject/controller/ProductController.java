package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.dto.ProductDTO;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
     * @param productDTO The product to create.
     * @return The created product.
     */
    @PostMapping(value = "/product/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        productDatabaseService.create(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    /**
     * Retrieves all products.
     *
     * @return The list of products.
     */
    @GetMapping(value = "/product/find/all")
    public ResponseEntity<List<ProductDTO>> findAllProducts() {
        List<ProductDTO> productList = productDatabaseService.findAllNotDeleted();
        return productList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productList);
    }

    /**
     * Retrieves a product by its UUID.
     *
     * @param uuid The UUID of the product.
     * @return The product.
     */
    @GetMapping(value = "/product/find/{uuid}")
    public ResponseEntity<ProductDTO> findProductByUuid(@PathVariable UUID uuid) {
        ProductDTO productDTO = productDatabaseService.findById(uuid);
        return ResponseEntity.ok(productDTO);
    }

    /**
     * Updates an existing product.
     *
     * @param uuid           The UUID of the product to update.
     * @param updatedProductDTO The updated product.
     * @return The updated product.
     */
    @PutMapping(value = "/product/update/{uuid}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable UUID uuid, @RequestBody ProductDTO updatedProductDTO) {
        productDatabaseService.update(uuid, updatedProductDTO);
        return ResponseEntity.ok(updatedProductDTO);
    }

    /**
     * Deletes a product by its UUID.
     *
     * @param uuid The UUID of the product to delete.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/product/delete/{uuid}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID uuid) {
        productDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
