package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for managing transactions.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionDatabaseService transactionDatabaseService;

    /**
     * Creates a new transaction.
     *
     * @param transaction The transaction to create.
     * @return The created transaction.
     */
    @PostMapping(value = "/transaction/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        log.info("endpoint request: create transaction");
        transactionDatabaseService.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    /**
     * Retrieves all transactions.
     *
     * @return The list of transactions.
     */
    @GetMapping(value = "/transaction/find/all")
    public ResponseEntity<List<Transaction>> findAllTransactions() {
        log.info("endpoint request: find all transactions");
        List<Transaction> transactionList = transactionDatabaseService.findAll();
        return createResponseEntity(transactionList);
    }

    /**
     * Retrieves a transaction by its UUID.
     *
     * @param uuid The UUID of the transaction.
     * @return The transaction.
     */
    @GetMapping(value = "/transaction/find/{uuid}")
    public ResponseEntity<Transaction> findTransactionByUuid(@PathVariable UUID uuid) {
        log.info("endpoint request: find transaction by uuid {}", uuid);
        Transaction transaction = transactionDatabaseService.findById(uuid);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Retrieves outgoing transactions for a specific UUID.
     *
     * @param uuid The UUID of the client.
     * @return The list of outgoing transactions.
     */
    @GetMapping(value = "/transaction/find/outgoing/{uuid}")
    public ResponseEntity<List<Transaction>> findOutgoingTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find transactions by uuid {}", uuid);
        List<Transaction> transactionList = transactionDatabaseService.findOutgoingTransactions(uuid);
        return createResponseEntity(transactionList);
    }

    /**
     * Retrieves incoming transactions for a specific UUID.
     *
     * @param uuid The UUID of the client.
     * @return The list of incoming transactions.
     */
    @GetMapping(value = "/transaction/find/incoming/{uuid}")
    public ResponseEntity<List<Transaction>> findIncomingTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find transactions by uuid {}", uuid);
        List<Transaction> transactionList = transactionDatabaseService.findIncomingTransactions(uuid);
        return createResponseEntity(transactionList);
    }

    /**
     * Transfers funds between accounts.
     *
     * @param transaction The transaction containing the transfer details.
     * @return A response indicating the success of the operation.
     */
    @PostMapping(value = "/transaction/transfer/")
    public ResponseEntity<String> transferFunds(@RequestBody Transaction transaction) {
        log.info("endpoint request: execute money transfer");
        transactionDatabaseService.transferFunds(transaction);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all transactions for a specific client UUID.
     *
     * @param uuid The UUID of the client.
     * @return The list of transactions.
     */
    @GetMapping(value = "/transaction/find-all-by-client/{uuid}")
    public ResponseEntity<List<Transaction>> findAllTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find all transactions by client id {}", uuid);
        List<Transaction> transactionList = transactionDatabaseService.findAllTransactionsByClientId(uuid);
        return createResponseEntity(transactionList);
    }

    /**
     * Retrieves a transaction statement for a specific client UUID and date range.
     *
     * @param uuid      The UUID of the client.
     * @param startDate The start date of the statement.
     * @param endDate   The end date of the statement.
     * @return The transaction statement.
     */
    @GetMapping(value = "/transaction/get/client/{uuid}/statement")
    public ResponseEntity<List<Transaction>> getTransactionStatement(
            @PathVariable("uuid") UUID uuid,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<Transaction> statement = transactionDatabaseService
                .findTransactionsByClientIdBetweenDates(uuid, startDate, endDate);
        return ResponseEntity.ok(statement);
    }

    /**
     * Retrieves a transaction statement for a date range.
     *
     * @param startDate The start date of the statement.
     * @param endDate   The end date of the statement.
     * @return The transaction statement.
     */
    @GetMapping(value = "/transaction/get/statement")
    public ResponseEntity<List<Transaction>> getTransactionStatement(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<Transaction> statement = transactionDatabaseService
                .findTransactionsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(statement);
    }

    /**
     * Creates a response entity for the given transaction list.
     * If the list is empty, returns a no content response, otherwise returns the transaction list.
     *
     * @param transactionList The list of transactions.
     * @return The response entity.
     */
    private ResponseEntity<List<Transaction>> createResponseEntity(List<Transaction> transactionList) {
        return transactionList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactionList);
    }
}
