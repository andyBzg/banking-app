package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for managing accounts.
 */
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountDatabaseService accountDatabaseService;

    /**
     * Create a new account.
     *
     * @param accountDto the account to create
     * @return the created account
     */
    @PostMapping(value = "/create")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto) {
        log.info("endpoint request: create account");
        accountDatabaseService.create(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
    }

    /**
     * Create a new account for a specific client.
     *
     * @param accountDto the account to create
     * @param uuid       the UUID of the client
     * @return the created account
     */
    @PostMapping(value = "/create/with-client-id/{uuid}")
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto accountDto, @PathVariable String uuid) {
        log.info("endpoint request: create account");
        accountDatabaseService.create(accountDto, uuid);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDto);
    }

    /**
     * Get all accounts.
     *
     * @return the list of all accounts
     */
    @GetMapping(value = "/find/all")
    public ResponseEntity<List<AccountDto>> findAllAccounts() {
        log.info("endpoint request: find all accounts");
        List<AccountDto> accountDtoList = accountDatabaseService.findAllNotDeleted();
        return accountDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accountDtoList);
    }

    /**
     * Find an account by its UUID.
     *
     * @param uuid the UUID of the account
     * @return the found account
     */
    @GetMapping(value = "/find/{uuid}")
    public ResponseEntity<AccountDto> findAccountByUuid(@PathVariable String uuid) {
        log.info("endpoint request: find account by uuid {}", uuid);
        AccountDto accountDto = accountDatabaseService.findDtoById(uuid);
        return ResponseEntity.ok(accountDto);
    }

    /**
     * Get all accounts by status.
     *
     * @param status the status of the accounts
     * @return the list of accounts with the specified status
     */
    @GetMapping(value = "/find/all/by-status/{status}")
    public ResponseEntity<List<AccountDto>> findAllAccountsByStatus(@PathVariable String status) {
        log.info("endpoint request: find all accounts by status {}", status);
        List<AccountDto> accountDtoList = accountDatabaseService.findAllByStatus(status);
        return accountDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accountDtoList);
    }

    /**
     * Update an account.
     *
     * @param uuid              the UUID of the account to update
     * @param updatedAccountDto the updated account data
     * @return the updated account
     */
    @PutMapping(value = "/update/{uuid}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String uuid, @RequestBody AccountDto updatedAccountDto) {
        log.info("endpoint request: update account uuid {}", uuid);
        accountDatabaseService.updateAccountDto(uuid, updatedAccountDto);
        return ResponseEntity.ok(updatedAccountDto);
    }

    /**
     * Delete an account.
     *
     * @param uuid the UUID of the account to delete
     * @return a response indicating a successful deletion
     */
    @DeleteMapping(value ="/delete/{uuid}")
    public ResponseEntity<String> deleteAccount(@PathVariable String uuid) {
        log.info("endpoint request: delete account uuid {}", uuid);
        accountDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    /**
     * Block all accounts associated with a client.
     *
     * @param uuid the UUID of the client
     * @return a response indicating a successful blocking of accounts
     */
    @PutMapping(value = "/block-all-client-accounts/{uuid}")
    public ResponseEntity<String> blockAllAccountsByClientUuid(@PathVariable String uuid) {
        log.info("endpoint request: block accounts by client uuid {}", uuid);
        accountDatabaseService.blockAccountsByClientUuid(uuid);
        return ResponseEntity.ok().build();
    }

    /**
     * Get all accounts by product ID and status.
     *
     * @param uuid   the UUID of the product
     * @param status the status of the accounts
     * @return the list of accounts with the specified product ID and status
     */
    @GetMapping(value = "/find/all/by-product/{uuid}/product-status/{status}")
    public ResponseEntity<List<AccountDto>> findAllAccountsByProductIdAndStatus(
            @PathVariable String uuid, @PathVariable String status) {
        log.info("endpoint request: find all accounts by product id {} and product status {}", uuid, status);
        List<AccountDto> accountDtoList = accountDatabaseService
                .findAccountsByProductIdAndStatus(uuid, status);
        return accountDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accountDtoList);
    }

    /**
     * Get all accounts by client UUID.
     *
     * @param uuid the UUID of the client
     * @return the list of accounts associated with the specified client UUID
     */
    @GetMapping(value = "/find/all/by-client-id/{uuid}")
    public ResponseEntity<List<AccountDto>> findAllAccountsByClientUuid(@PathVariable String uuid) {
        log.info("endpoint request: find all accounts by client id {} ", uuid);
        List<AccountDto> accountDtoList = accountDatabaseService.findAllDtoByClientId(uuid);
        return accountDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accountDtoList);
    }
}
