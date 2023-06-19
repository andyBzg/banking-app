package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductDatabaseService productDatabaseService;


    @PostMapping(value = "/product/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productDatabaseService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @GetMapping(value = "/product/find/all")
    public ResponseEntity<List<Product>> findAllProducts() {
        List<Product> productList = productDatabaseService.findAll();
        if (productList != null && !productList.isEmpty()) {
            return ResponseEntity.ok(productList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/product/find/{uuid}")
    public ResponseEntity<Product> findProductByUuid(@PathVariable UUID uuid) {
        Product product = productDatabaseService.findById(uuid);
        return ResponseEntity.ok(product);
    }

    @PutMapping(value = "/product/update/{uuid}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID uuid, @RequestBody Product updatedProduct) {
        productDatabaseService.update(uuid, updatedProduct);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping(value ="/product/delete/{uuid}")
    public ResponseEntity<String> deleteProduct(@PathVariable UUID uuid) {
        productDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
