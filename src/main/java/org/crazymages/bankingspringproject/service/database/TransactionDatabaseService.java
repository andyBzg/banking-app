package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.dto.TransactionDTO;

import java.util.List;
import java.util.UUID;

/**
 * A service interface for managing Transaction entities in the database.
 * It provides methods for creating, retrieving, updating, and deleting Transaction entities.
 */
public interface TransactionDatabaseService {

    /**
     * Creates a new Transaction entity in the database.
     *
     * @param transactionDTO The Transaction entity to be created.
     */
    void create(TransactionDTO transactionDTO);

    /**
     * Retrieves all Transaction entities from the database.
     *
     * @return A list of all Transaction entities.
     */
    List<TransactionDTO> findAll();

    /**
     * Retrieves a Transaction entity from the database by its UUID.
     *
     * @param uuid The UUID of the Transaction to retrieve.
     * @return The Transaction entity with the specified UUID, or null if not found.
     */
    TransactionDTO findById(UUID uuid);

    /**
     * Retrieves all outgoing Transaction entities associated with the specified UUID.
     *
     * @param uuid The UUID of the client/account.
     * @return A list of outgoing Transaction entities.
     */
    List<TransactionDTO> findOutgoingTransactions(UUID uuid);

    /**
     * Retrieves all incoming Transaction entities associated with the specified UUID.
     *
     * @param uuid The UUID of the client/account.
     * @return A list of incoming Transaction entities.
     */
    List<TransactionDTO> findIncomingTransactions(UUID uuid);

    /**
     * Retrieves all Transaction entities associated with the specified client UUID.
     *
     * @param uuid The UUID of the client.
     * @return A list of Transaction entities.
     */
    List<TransactionDTO> findAllTransactionsByClientId(UUID uuid);

    /**
     * Transfers funds between accounts based on the provided Transaction entity.
     *
     * @param transactionDTO The Transaction entity representing the fund transfer.
     */
    void transferFunds(TransactionDTO transactionDTO);

    /**
     * Retrieves all Transaction entities associated with the specified client UUID between the specified dates.
     *
     * @param clientUuid The UUID of the client.
     * @param from       The starting date (inclusive) in the format "yyyy-MM-dd".
     * @param to         The ending date (inclusive) in the format "yyyy-MM-dd".
     * @return A list of Transaction entities within the specified date range.
     */
    List<TransactionDTO> findTransactionsByClientIdBetweenDates(UUID clientUuid, String from, String to);

    /**
     * Retrieves all Transaction entities between the specified dates.
     *
     * @param from The starting date (inclusive) in the format "yyyy-MM-dd".
     * @param to   The ending date (inclusive) in the format "yyyy-MM-dd".
     * @return A list of Transaction entities within the specified date range.
     */
    List<TransactionDTO> findTransactionsBetweenDates(String from, String to);
}
