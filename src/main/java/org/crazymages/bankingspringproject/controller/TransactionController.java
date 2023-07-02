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

@RestController
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionDatabaseService transactionDatabaseService;


    @PostMapping(value = "/transaction/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        log.info("endpoint request: create transaction");
        transactionDatabaseService.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping(value = "/transaction/find/all")
    public ResponseEntity<List<Transaction>> findAllTransactions() {
        log.info("endpoint request: find all transactions");
        List<Transaction> transactionList = transactionDatabaseService.findAll();
        return createResponseEntity(transactionList);
    }

    @GetMapping(value = "/transaction/find/{uuid}")
    public ResponseEntity<Transaction> findTransactionByUuid(@PathVariable UUID uuid) {
        log.info("endpoint request: find transaction by uuid {}", uuid);
        Transaction transaction = transactionDatabaseService.findById(uuid);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping(value = "/transaction/find/outgoing/{uuid}")
    public ResponseEntity<List<Transaction>> findOutgoingTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find transactions by uuid {}", uuid);
        List<Transaction> transactionList = transactionDatabaseService.findOutgoingTransactions(uuid);
        return createResponseEntity(transactionList);
    }

    @GetMapping(value = "/transaction/find/incoming/{uuid}")
    public ResponseEntity<List<Transaction>> findIncomingTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find transactions by uuid {}", uuid);
        List<Transaction> transactionList = transactionDatabaseService.findIncomingTransactions(uuid);
        return createResponseEntity(transactionList);
    }

    /**
     * Перевод средств с одного банковского аккаунта на другой.
     **/
    @PostMapping(value = "/transaction/transfer/")
    public ResponseEntity<String> transferFunds(@RequestBody Transaction transaction) {
        log.info("endpoint request: execute money transfer");
        transactionDatabaseService.transferFunds(transaction);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/transaction/find-all-by-client/{uuid}")
    public ResponseEntity<List<Transaction>> findAllTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find all transactions by client id {} ", uuid);
        List<Transaction> transactionList = transactionDatabaseService.findAllTransactionsByClientId(uuid);
        return createResponseEntity(transactionList);
    }

    private ResponseEntity<List<Transaction>> createResponseEntity(List<Transaction> transactionList) {
        return transactionList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(transactionList);
    }

    @GetMapping(value = "/transaction/get/client/{uuid}/statement")
    public ResponseEntity<List<Transaction>> getTransactionStatement(
            @PathVariable("uuid") UUID uuid,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<Transaction> statement = transactionDatabaseService
                .findTransactionsByClientIdBetweenDates(uuid, startDate, endDate);
        return ResponseEntity.ok(statement);
    }

    @GetMapping(value = "/transaction/get/statement")
    public ResponseEntity<List<Transaction>> getTransactionStatement(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<Transaction> statement = transactionDatabaseService
                .findTransactionsBetweenDates(startDate, endDate);
        return ResponseEntity.ok(statement);
    }
}
