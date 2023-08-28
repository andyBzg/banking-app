package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.dto.account.AccountCreationDto;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.dto.account.AccountDto;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.entity.enums.ProductType;

import java.util.List;
import java.util.UUID;

/**
 * A service interface for managing Account entities in the database.
 * It provides methods for creating, retrieving, updating, and deleting Account entities.
 */
public interface AccountDatabaseService {

    /**
     * Creates a new Account entity in the database with the specified Client UUID.
     *
     * @param accountDto The Account entity to be created.
     * @param clientUuid The Client UUID to be assigned to the Account entity.
     */
    void create(AccountCreationDto accountDto, String clientUuid);

    /**
     * Retrieves an AccountDto entity from the database by its UUID.
     *
     * @param uuid The UUID of the Account entity to retrieve.
     * @return The retrieved Account Dto entity, or null if not found.
     */
    AccountDto findDtoById(String uuid);

    /**
     * Retrieves an Account entity from the database by its UUID.
     *
     * @param uuid The UUID of the Account entity to retrieve.
     * @return The retrieved Account entity, or null if not found.
     */
    Account findById(UUID uuid);

    /**
     * Retrieves all non-deleted Account entities from the database.
     *
     * @return A list of all non-deleted Account entities.
     */
    List<AccountDto> findAllNotDeleted();

    /**
     * Retrieves all deleted Account entities from the database.
     *
     * @return A list of all deleted Account entities.
     */
    List<AccountDto> findDeletedAccounts();

    /**
     * Retrieves all Account entities from the database with the specified status.
     *
     * @param status The status to filter Account entities by.
     * @return A list of Account entities with the specified status.
     */
    List<AccountDto> findAllByStatus(String status);

    /**
     * Updates an Account entity in the database with the specified UUID.
     *
     * @param uuid              The UUID of the Account entity to update.
     * @param updatedAccountDto The Account entity containing the updated values.
     */
    void updateAccountDto(String uuid, AccountDto updatedAccountDto);

    /**
     * Updates an Account entity in the database with the specified UUID.
     *
     * @param uuid    The UUID of the Account entity to update.
     * @param account The Account entity containing the updated values.
     */
    void update(UUID uuid, Account account);

    /**
     * Deletes an Account entity from the database by its UUID.
     *
     * @param uuid The UUID of the Account entity to delete.
     */
    void delete(String uuid);

    /**
     * Blocks all accounts associated with a client UUID in the database.
     *
     * @param clientUuid The UUID of the client whose accounts should be blocked.
     */
    void blockAccountsByClientUuid(String clientUuid);

    /**
     * Retrieves all Account entities from the database with the specified product UUID and status.
     *
     * @param productUuid The UUID of the product to filter Account entities by.
     * @param status      The status of the product to filter Account entities by.
     * @return A list of Account entities with the specified product UUID and status.
     */
    List<AccountDto> findAccountsByProductIdAndStatus(String productUuid, String status);

    /**
     * Retrieves all AccountDTOs from the database associated with a client UUID.
     *
     * @param clientUuid The UUID of the client to filter AccountDTOs by.
     * @return A list of AccountDTOs associated with the specified client UUID.
     */
    List<AccountDto> findAllDtoByClientId(String clientUuid);

    /**
     * Retrieves all Account entities from the database associated with a client UUID.
     *
     * @param clientUuid The UUID of the client to filter Account entities by.
     * @return A list of Account entities associated with the specified client UUID.
     */
    List<Account> findAllByClientId(UUID clientUuid);

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

    /**
     * Retrieves all Account entities from the database with the specified product type and status.
     *
     * @param productType   The type of the product to filter Account entities by.
     * @param productStatus The status of the product to filter Account entities by.
     * @return A list of Account entities with the specified product type and status.
     */
    List<Account> findAccountsByProductTypeAndStatus(ProductType productType, ProductStatus productStatus);
}
