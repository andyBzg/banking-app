package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.dto.ProductDTO;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;

import java.util.List;
import java.util.UUID;

/**
 * A service interface for managing Product entities in the database.
 * It provides methods for creating, retrieving, updating, and deleting Product entities.
 */
public interface ProductDatabaseService {

    /**
     * Creates a new Product entity in the database.
     *
     * @param productDTO The Product entity to be created.
     */
    void create(ProductDTO productDTO);

    /**
     * Retrieves all Product entities from the database.
     *
     * @return A list of all Product entities.
     */
    List<ProductDTO> findAll();

    /**
     * Retrieves all non-deleted Product entities from the database.
     *
     * @return A list of all non-deleted Product entities.
     */
    List<ProductDTO> findAllNotDeleted();

    /**
     * Retrieves all deleted Product entities from the database.
     *
     * @return A list of all deleted Product entities.
     */
    List<ProductDTO> findDeletedProducts();

    /**
     * Retrieves a Product entity from the database by its UUID.
     *
     * @param uuid The UUID of the Product to retrieve.
     * @return The Product entity with the specified UUID, or null if not found.
     */
    ProductDTO findById(UUID uuid);

    /**
     * Retrieves a Product entity from the database by its type, status, and currency code.
     *
     * @param type         The type of the Product.
     * @param status       The status of the Product.
     * @param currencyCode The currency code of the Product.
     * @return The Product entity that matches the specified type, status, and currency code, or null if not found.
     */
    Product findProductByTypeAndStatusAndCurrencyCode(
            ProductType type, ProductStatus status, CurrencyCode currencyCode);

    /**
     * Updates a Product entity in the database with the specified UUID.
     *
     * @param uuid              The UUID of the Product to update.
     * @param updatedProductDTO The updated Product entity.
     */
    void update(UUID uuid, ProductDTO updatedProductDTO);

    /**
     * Deletes a Product entity from the database with the specified UUID.
     *
     * @param uuid The UUID of the Product to delete.
     */
    void delete(UUID uuid);
}
