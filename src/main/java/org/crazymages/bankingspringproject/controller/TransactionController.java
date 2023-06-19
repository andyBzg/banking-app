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
        if (transactionList != null && !transactionList.isEmpty()) {
            return ResponseEntity.ok(transactionList);
        } else {
            return ResponseEntity.noContent().build();
        }
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
        if (transactionList != null && !transactionList.isEmpty()) {
            return ResponseEntity.ok(transactionList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/transaction/find/incoming/{uuid}")
    public ResponseEntity<List<Transaction>> findIncomingTransactions(@PathVariable UUID uuid) {
        log.info("endpoint request: find transactions by uuid {}", uuid);
        List<Transaction> transactionList = transactionDatabaseService.findIncomingTransactions(uuid);
        if (transactionList != null && !transactionList.isEmpty()) {
            return ResponseEntity.ok(transactionList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * Перевод средств с одного банковского аккаунта на другой.
     **/
    @PostMapping(value = "/transaction/transfer/")
    public ResponseEntity<String> transferFunds(@RequestBody Transaction transaction) {
        transactionDatabaseService.transferFunds(transaction);
        return ResponseEntity.ok().build();
    }


//    GET /transactions/get/statement?startDate=2022-01-01&endDate=2022-12-31
//    @GetMapping(value = "/transaction/get/statement")
//    public ResponseEntity<List<Transaction>> getTransactionStatement(
//            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//
//        // Здесь выполняется логика получения выписки по транзакциям за указанный промежуток времени
//        List<Transaction> statement = transactionDatabaseService.findAll();
//        return ResponseEntity.ok(statement);
//    }
}
