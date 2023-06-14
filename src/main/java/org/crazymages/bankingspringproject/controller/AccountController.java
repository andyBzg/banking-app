package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountDatabaseService accountDatabaseService;


    @PostMapping(value = "/account/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        accountDatabaseService.create(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping(value = "/account/find/all")
    public ResponseEntity<List<Account>> findAllAccounts() {
        List<Account> accountList = accountDatabaseService.findAll();
        if (accountList != null && !accountList.isEmpty()) {
            return ResponseEntity.ok(accountList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/account/find/{uuid}")
    public ResponseEntity<Account> findAccountByUuid(@PathVariable UUID uuid) {
        Account account = accountDatabaseService.findById(uuid);
        return ResponseEntity.ok(account);
    }

    @GetMapping(value = "/account/find/all/by-status/{status}")
    public ResponseEntity<List<Account>> findAllAccountsByStatus(@PathVariable String status) {
        List<Account> accountList = accountDatabaseService.findAllByStatus(status);
        if (accountList != null && !accountList.isEmpty()) {
            return ResponseEntity.ok(accountList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping(value = "/account/update/{uuid}")
    public ResponseEntity<Account> updateAccount(@PathVariable UUID uuid, @RequestBody Account account) {
        Account accountUpdate = accountDatabaseService.update(uuid, account);
        return ResponseEntity.ok(accountUpdate);
    }

    @DeleteMapping(value ="/account/delete/{uuid}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID uuid) {
        accountDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
