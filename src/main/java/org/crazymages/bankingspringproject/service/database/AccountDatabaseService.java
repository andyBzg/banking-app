package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.dto.AccountDTO;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;

import java.util.List;
import java.util.UUID;

/**
 * A service interface for managing Account entities in the database.
 * It provides methods for creating, retrieving, updating, and deleting Account entities.
 */
public interface AccountDatabaseService {

    /**
     * Creates a new Account entity in the database.
     *
     * @param accountDTO The Account entity to be created.
     */
    void create(AccountDTO accountDTO);

    /**
     * Creates a new Account entity in the database with the specified Client UUID.
     *
     * @param accountDTO    The Account entity to be created.
     * @param clientUuid The Client UUID to be assigned to the Account entity.
     */
    void create(AccountDTO accountDTO, UUID clientUuid);

    /**
     * Retrieves an Account entity from the database by its UUID.
     *
     * @param uuid The UUID of the Account entity to retrieve.
     * @return The retrieved Account entity, or null if not found.
     */
    AccountDTO findById(UUID uuid);

    /**
     * Retrieves all non-deleted Account entities from the database.
     *
     * @return A list of all non-deleted Account entities.
     */
    List<AccountDTO> findAllNotDeleted();

    /**
     * Retrieves all deleted Account entities from the database.
     *
     * @return A list of all deleted Account entities.
     */
    List<AccountDTO> findDeletedAccounts();

    /**
     * Retrieves all Account entities from the database with the specified status.
     *
     * @param status The status to filter Account entities by.
     * @return A list of Account entities with the specified status.
     */
    List<AccountDTO> findAllByStatus(String status);

    /**
     * Updates an Account entity in the database with the specified UUID.
     *
     * @param uuid              The UUID of the Account entity to update.
     * @param updatedAccountDTO The Account entity containing the updated values.
     */
    void update(UUID uuid, AccountDTO updatedAccountDTO);

    /**
     * Deletes an Account entity from the database by its UUID.
     *
     * @param uuid The UUID of the Account entity to delete.
     */
    void delete(UUID uuid);

    /**
     * Blocks all accounts associated with a client UUID in the database.
     *
     * @param clientUuid The UUID of the client whose accounts should be blocked.
     */
    void blockAccountsByClientUuid(UUID clientUuid);

    /**
     * Retrieves all Account entities from the database with the specified product UUID and status.
     *
     * @param productUuid The UUID of the product to filter Account entities by.
     * @param status      The status of the product to filter Account entities by.
     * @return A list of Account entities with the specified product UUID and status.
     */
    List<AccountDTO> findAccountsByProductIdAndStatus(UUID productUuid, ProductStatus status);

    /**
     * Retrieves all Account entities from the database associated with a client UUID.
     *
     * @param clientUuid The UUID of the client to filter Account entities by.
     * @return A list of Account entities associated with the specified client UUID.
     */
    List<AccountDTO> findAllByClientId(UUID clientUuid);

    /**
     * Retrieves the current Account entity from the database associated with a client UUID.
     *
     * @param clientUuid The UUID of the client to retrieve the current Account entity for.
     * @return The current Account entity associated with the specified client UUID, or null if not found.
     */
    Account findCurrentByClientId(UUID clientUuid);

    /**
     * Retrieves the savings Account entity from the database associated with a client UUID.
     *
     * @param clientUuid The UUID of the client to retrieve the savings Account entity for.
     * @return The savings Account entity associated with the specified client UUID, or null if not found.
     */
    Account findSavingsByClientId(UUID clientUuid);
}
