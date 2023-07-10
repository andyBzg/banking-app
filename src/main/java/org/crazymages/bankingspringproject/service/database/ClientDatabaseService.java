package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.dto.ClientDTO;
import org.crazymages.bankingspringproject.entity.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * A service interface for managing Client entities in the database.
 * It provides methods for creating, retrieving, updating, and deleting Client entities.
 */
public interface ClientDatabaseService {

    /**
     * Creates a new Client entity in the database.
     *
     * @param clientDTO The Client entity to be created.
     */
    void create(ClientDTO clientDTO);

    /**
     * Retrieves all Client entities from the database.
     *
     * @return A list of all ClientDTOs.
     */
    List<ClientDTO> findAll();

    /**
     * Retrieves all non-deleted Client entities from the database.
     *
     * @return A list of all non-deleted Client entities.
     */
    List<ClientDTO> findAllNotDeleted();

    /**
     * Retrieves all deleted Client entities from the database.
     *
     * @return A list of all deleted Client entities.
     */
    List<ClientDTO> findDeletedClients();

    /**
     * Retrieves a Client entity from the database by its UUID.
     *
     * @param uuid The UUID of the Client to retrieve.
     * @return The ClientDTO with the specified UUID, or null if not found.
     */
    ClientDTO findById(UUID uuid);

    /**
     * Updates a Client entity in the database with the specified UUID.
     *
     * @param uuid             The UUID of the Client to update.
     * @param updatedClientDTO The updated ClientDTO.
     */
    void update(UUID uuid, ClientDTO updatedClientDTO);

    /**
     * Deletes a Client entity from the database with the specified UUID.
     *
     * @param uuid The UUID of the Client to delete.
     */
    void delete(UUID uuid);

    /**
     * Retrieves all active Client entities from the database.
     *
     * @return A list of all active ClientDTOs.
     */
    List<ClientDTO> findActiveClients();

    /**
     * Retrieves all Client entities from the database where the balance is greater than the specified amount.
     *
     * @param balance The minimum balance amount to filter by.
     * @return A list of ClientDTOs where the balance is greater than the specified amount.
     */
    List<ClientDTO> findClientsWhereBalanceMoreThan(BigDecimal balance);

    /**
     * Retrieves all Client entities from the database where the transaction count is greater than the specified amount.
     *
     * @param count The minimum transaction count to filter by.
     * @return A list of ClientDTOs where the transaction count is greater than the specified amount.
     */
    List<ClientDTO> findClientsWhereTransactionMoreThan(Integer count);

    /**
     * Calculates the total balance of all Accounts associated with the specified client UUID.
     *
     * @param uuid The UUID of the client to calculate the total balance for.
     * @return The total balance of all Accounts associated with the specified client UUID.
     */
    BigDecimal calculateTotalBalanceByClientUuid(UUID uuid);

    /**
     * Checks if the Client with the specified UUID has an active status.
     *
     * @param uuid The UUID of the client to check.
     * @return True if the Client has an active status, false otherwise.
     */
    boolean isClientStatusActive(UUID uuid);

    /**
     * Retrieves all Client entities from the database that have both current and savings Accounts.
     *
     * @return A list of Client entities that have both current and savings Accounts.
     */
    List<Client> findClientsWithCurrentAndSavingsAccounts();
}
