package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.dto.client.ClientDto;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;

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
    void create(ClientDto clientDTO);

    /**
     * Saves a Client entity in the database.
     *
     * @param client The Client entity to be saved.
     */
    void save(Client client);

    /**
     * Retrieves all Client entities from the database.
     *
     * @return A list of all ClientDTOs.
     */
    List<ClientDto> findAll();

    /**
     * Retrieves all non-deleted Client entities from the database.
     *
     * @return A list of all non-deleted Client entities.
     */
    List<ClientDto> findAllNotDeleted();

    /**
     * Retrieves all deleted Client entities from the database.
     *
     * @return A list of all deleted Client entities.
     */
    List<ClientDto> findDeletedClients();

    /**
     * Retrieves a Client entity from the database by its UUID.
     *
     * @param uuid The UUID of the Client to retrieve.
     * @return The ClientDTO with the specified UUID, or null if not found.
     */
    ClientDto findById(String uuid);

    /**
     * Retrieves a Client entity from the database by its email.
     *
     * @param email The email of the Client to retrieve.
     * @return The Client entity with the specified email, or null if not found.
     */
    Client findClientByEmail(String email);

    /**
     * Updates a Client entity in the database with the specified UUID.
     *
     * @param uuid             The UUID of the Client to update.
     * @param updatedClientDto The updated ClientDTO.
     */
    void update(String uuid, ClientDto updatedClientDto);

    /**
     * Deletes a Client entity from the database with the specified UUID.
     *
     * @param uuid The UUID of the Client to delete.
     */
    void delete(String uuid);

    /**
     * Retrieves all active Client entities from the database.
     *
     * @return A list of all active ClientDTOs.
     */
    List<ClientDto> findActiveClients();

    /**
     * Retrieves all Client entities from the database where the balance is greater than the specified amount.
     *
     * @param balance The minimum balance amount to filter by.
     * @return A list of ClientDTOs where the balance is greater than the specified amount.
     */
    List<ClientDto> findClientsWhereBalanceMoreThan(BigDecimal balance);

    /**
     * Retrieves all Client entities from the database where the transaction count is greater than the specified amount.
     *
     * @param count The minimum transaction count to filter by.
     * @return A list of ClientDTOs where the transaction count is greater than the specified amount.
     */
    List<ClientDto> findClientsWhereTransactionMoreThan(Integer count);

    /**
     * Calculates the total balance of all Accounts associated with the specified client UUID.
     *
     * @param uuid The UUID of the client to calculate the total balance for.
     * @return The total balance of all Accounts associated with the specified client UUID.
     */
    BigDecimal calculateTotalBalanceByClientUuid(String uuid);

    /**
     * Checks if the Client with the specified UUID has an active status.
     *
     * @param uuid The UUID of the client to check.
     * @return True if the Client has an active status, false otherwise.
     */
    boolean isClientStatusBlocked(UUID uuid);

    /**
     * Retrieves all Client entities from the database that have both current and savings Accounts.
     *
     * @return A list of Client entities that have both current and savings Accounts.
     */
    List<Client> findClientsWithCurrentAndSavingsAccounts();

    /**
     * Retrieves all Client entities from the database that have the specified status.
     *
     * @param status The status to filter by.
     * @return A list of Client entities that have the specified status.
     */
    List<Client> findClientsByStatus(ClientStatus status);

    /**
     * Blocks the Client with the specified UUID by setting its status to blocked.
     *
     * @param uuid The UUID of the client to block.
     */
    void blockClientById(String uuid);
}
