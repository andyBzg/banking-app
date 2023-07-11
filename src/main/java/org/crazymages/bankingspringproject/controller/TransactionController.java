package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.TransactionDTO;
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
     * @param transactionDTO The transaction to create.
     * @return The created transaction.
     */
    @PostMapping(value = "/transaction/create")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        log.info("endpoint request: create transaction");
        transactionDatabaseService.create(transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO);
    }

    /**
     * Retrieves all transactions.
     *
     * @return The list of transactions.
     */
    @GetMapping(value = "/transaction/find/all")
    public ResponseEntity<List<TransactionDTO>> findAllTransactions() {
        log.info("endpoint request: find all transactions");
        List<TransactionDTO> transactionList = transactionDatabaseService.findAll();
        return transactionList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactionList);
    }

    /**
     * Retrieves a transaction by its UUID.
     *
     * @param uuid The UUID of the transaction.
     * @return The transaction.
     */
    @GetMapping(value = "/transaction/find/{uuid}")
    public ResponseEntity<TransactionDTO> findTransactionByUuid(@PathVariable UUID uuid) {
        log.info("endpoint request: find transaction by uuid {}", uuid);
        TransactionDTO transaction = transactionDatabaseService.findById(uuid);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Retrieves outgoing transactions for a specific UUID.
     *
     * @param uuid The UUID of the client.
     * @return The list of outgoing transactions.
     */
    @GetMapping(value = "/transaction/find/outgoing/{uuid}")
    public ResponseEntity<List<TransactionDTO>> findOutgoingTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find transactions by uuid {}", uuid);
        List<TransactionDTO> transactionList = transactionDatabaseService.findOutgoingTransactions(uuid);
        return transactionList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactionList);
    }

    /**
     * Retrieves incoming transactions for a specific UUID.
     *
     * @param uuid The UUID of the client.
     * @return The list of incoming transactions.
     */
    @GetMapping(value = "/transaction/find/incoming/{uuid}")
    public ResponseEntity<List<TransactionDTO>> findIncomingTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find transactions by uuid {}", uuid);
        List<TransactionDTO> transactionList = transactionDatabaseService.findIncomingTransactions(uuid);
        return transactionList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactionList);
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
    public ResponseEntity<List<TransactionDTO>> findAllTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find all transactions by client id {}", uuid);
        List<TransactionDTO> transactionList = transactionDatabaseService.findAllTransactionsByClientId(uuid);
        return transactionList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactionList);
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
    public ResponseEntity<List<TransactionDTO>> getTransactionStatement(
            @PathVariable("uuid") UUID uuid,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<TransactionDTO> statement = transactionDatabaseService
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
    public ResponseEntity<List<TransactionDTO>> getTransactionStatement(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<TransactionDTO> statement = transactionDatabaseService
                .findTransactionsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(statement);
    }
}
