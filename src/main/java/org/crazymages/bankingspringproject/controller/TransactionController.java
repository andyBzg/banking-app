package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionDatabaseService transactionDatabaseService;


    @PostMapping(value = "/transaction/create")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        transactionDatabaseService.create(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping(value = "/transaction/find/all")
    public ResponseEntity<List<Transaction>> findAllTransactions() {
        List<Transaction> transactionList = transactionDatabaseService.findAll();
        if (transactionList != null && !transactionList.isEmpty()) {
            return ResponseEntity.ok(transactionList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/transaction/find/{uuid}")
    public ResponseEntity<Transaction> findTransactionByUuid(@PathVariable UUID uuid) {
        Transaction transaction = transactionDatabaseService.findById(uuid);
        return transaction != null ? ResponseEntity.ok(transaction) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value ="/transaction/delete/{uuid}")
    public ResponseEntity<String> deleteTransaction(@PathVariable UUID uuid) {
        transactionDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
