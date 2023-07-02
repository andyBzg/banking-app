package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountDatabaseService accountDatabaseService;


    @PostMapping(value = "/account/create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        log.info("endpoint request: create account");
        accountDatabaseService.create(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PostMapping(value = "/account/create/by-client/{uuid}")
    public ResponseEntity<Account> createAccount(@RequestBody Account account, @PathVariable UUID uuid) {
        log.info("endpoint request: create account");
        accountDatabaseService.create(account, uuid);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping(value = "/account/find/all")
    public ResponseEntity<List<Account>> findAllAccounts() {
        log.info("endpoint request: find all accounts");
        List<Account> accountList = accountDatabaseService.findAllNotDeleted();
        return createResponseEntity(accountList);
    }

    @GetMapping(value = "/account/find/{uuid}")
    public ResponseEntity<Account> findAccountByUuid(@PathVariable UUID uuid) {
        log.info("endpoint request: find account by uuid {}", uuid);
        Account account = accountDatabaseService.findById(uuid);
        return ResponseEntity.ok(account);
    }

    @GetMapping(value = "/account/find/all/by-status/{status}")
    public ResponseEntity<List<Account>> findAllAccountsByStatus(@PathVariable String status) {
        log.info("endpoint request: find all accounts by status {}", status);
        List<Account> accountList = accountDatabaseService.findAllByStatus(status);
        return createResponseEntity(accountList);
    }

    @PutMapping(value = "/account/update/{uuid}")
    public ResponseEntity<Account> updateAccount(@PathVariable UUID uuid, @RequestBody Account updatedAccount) {
        log.info("endpoint request: update account uuid {}", uuid);
        accountDatabaseService.update(uuid, updatedAccount);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping(value ="/account/delete/{uuid}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID uuid) {
        log.info("endpoint request: delete account uuid {}", uuid);
        accountDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/account/block-all-client-accounts/{uuid}")
    public ResponseEntity<String> blockAllAccountsByClientUuid(@PathVariable UUID uuid) {
        log.info("endpoint request: block accounts by client uuid {}", uuid);
        accountDatabaseService.blockAccountsByClientUuid(uuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/account/find/all/by-product/{uuid}/product-status/{status}")
    public ResponseEntity<List<Account>> findAllAccountsByProductIdAndStatus(
            @PathVariable UUID uuid, @PathVariable ProductStatus status) {
        log.info("endpoint request: find all accounts by product id {} and product status {}", uuid, status);
        List<Account> accountList = accountDatabaseService.findAccountsByProductIdAndStatus(uuid, status);
        return createResponseEntity(accountList);
    }

    @GetMapping(value = "/account/find/all/by-client/{uuid}")
    public ResponseEntity<List<Account>> findAllAccountsByClientUuid(@PathVariable UUID uuid) {
        log.info("endpoint request: find all accounts by client id {} ", uuid);
        List<Account> accountList = accountDatabaseService.findAllByClientId(uuid);
        return createResponseEntity(accountList);
     }

    private ResponseEntity<List<Account>> createResponseEntity(List<Account> accountList) {
        return accountList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accountList);
    }
}
