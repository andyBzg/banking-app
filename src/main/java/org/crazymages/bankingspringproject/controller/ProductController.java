package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Product;
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
     * @param product The product to create.
     * @return The created product.
     */
    @PostMapping(value = "/product/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productDatabaseService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    /**
     * Retrieves all products.
     *
     * @return The list of products.
     */
    @GetMapping(value = "/product/find/all")
    public ResponseEntity<List<Product>> findAllProducts() {
        List<Product> productList = productDatabaseService.findAllNotDeleted();
        return productList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productList);
    }

    /**
     * Retrieves a product by its UUID.
     *
     * @param uuid The UUID of the product.
     * @return The product.
     */
    @GetMapping(value = "/product/find/{uuid}")
    public ResponseEntity<Product> findProductByUuid(@PathVariable UUID uuid) {
        Product product = productDatabaseService.findById(uuid);
        return ResponseEntity.ok(product);
    }

    /**
     * Updates an existing product.
     *
     * @param uuid           The UUID of the product to update.
     * @param updatedProduct The updated product.
     * @return The updated product.
     */
    @PutMapping(value = "/product/update/{uuid}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID uuid, @RequestBody Product updatedProduct) {
        productDatabaseService.update(uuid, updatedProduct);
        return ResponseEntity.ok(updatedProduct);
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
