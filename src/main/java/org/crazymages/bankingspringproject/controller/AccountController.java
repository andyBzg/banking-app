package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AccountDTO;
import org.crazymages.bankingspringproject.entity.enums.ProductStatus;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for managing accounts.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountDatabaseService accountDatabaseService;

    /**
     * Create a new account.
     *
     * @param accountDTO the account to create
     * @return the created account
     */
    @PostMapping(value = "/account/create")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        log.info("endpoint request: create account");
        accountDatabaseService.create(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }

    /**
     * Create a new account for a specific client.
     *
     * @param accountDTO the account to create
     * @param uuid       the UUID of the client
     * @return the created account
     */
    @PostMapping(value = "/account/create/by-client/{uuid}")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO, @PathVariable UUID uuid) {
        log.info("endpoint request: create account");
        accountDatabaseService.create(accountDTO, uuid);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountDTO);
    }

    /**
     * Get all accounts.
     *
     * @return the list of all accounts
     */
    @GetMapping(value = "/account/find/all")
    public ResponseEntity<List<AccountDTO>> findAllAccounts() {
        log.info("endpoint request: find all accounts");
        List<AccountDTO> accountDTOs = accountDatabaseService.findAllNotDeleted();
        return accountDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accountDTOs);
    }

    /**
     * Find an account by its UUID.
     *
     * @param uuid the UUID of the account
     * @return the found account
     */
    @GetMapping(value = "/account/find/{uuid}")
    public ResponseEntity<AccountDTO> findAccountByUuid(@PathVariable UUID uuid) {
        log.info("endpoint request: find account by uuid {}", uuid);
        AccountDTO accountDto = accountDatabaseService.findById(uuid);
        return ResponseEntity.ok(accountDto);
    }

    /**
     * Get all accounts by status.
     *
     * @param status the status of the accounts
     * @return the list of accounts with the specified status
     */
    @GetMapping(value = "/account/find/all/by-status/{status}")
    public ResponseEntity<List<AccountDTO>> findAllAccountsByStatus(@PathVariable String status) {
        log.info("endpoint request: find all accounts by status {}", status);
        List<AccountDTO> accountList = accountDatabaseService.findAllByStatus(status);
        return accountList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accountList);
    }

    /**
     * Update an account.
     *
     * @param uuid              the UUID of the account to update
     * @param updatedAccountDto the updated account data
     * @return the updated account
     */
    @PutMapping(value = "/account/update/{uuid}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable UUID uuid, @RequestBody AccountDTO updatedAccountDto) {
        log.info("endpoint request: update account uuid {}", uuid);
        accountDatabaseService.update(uuid, updatedAccountDto);
        return ResponseEntity.ok(updatedAccountDto);
    }

    /**
     * Delete an account.
     *
     * @param uuid the UUID of the account to delete
     * @return a response indicating a successful deletion
     */
    @DeleteMapping(value ="/account/delete/{uuid}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID uuid) {
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
    @PutMapping(value = "/account/block-all-client-accounts/{uuid}")
    public ResponseEntity<String> blockAllAccountsByClientUuid(@PathVariable UUID uuid) {
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
    @GetMapping(value = "/account/find/all/by-product/{uuid}/product-status/{status}")
    public ResponseEntity<List<AccountDTO>> findAllAccountsByProductIdAndStatus(
            @PathVariable UUID uuid, @PathVariable ProductStatus status) {
        log.info("endpoint request: find all accounts by product id {} and product status {}", uuid, status);
        List<AccountDTO> accountList = accountDatabaseService.findAccountsByProductIdAndStatus(uuid, status);
        return accountList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accountList);
    }

    /**
     * Get all accounts by client UUID.
     *
     * @param uuid the UUID of the client
     * @return the list of accounts associated with the specified client UUID
     */
    @GetMapping(value = "/account/find/all/by-client/{uuid}")
    public ResponseEntity<List<AccountDTO>> findAllAccountsByClientUuid(@PathVariable UUID uuid) {
        log.info("endpoint request: find all accounts by client id {} ", uuid);
        List<AccountDTO> accountList = accountDatabaseService.findAllByClientId(uuid);
        return accountList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(accountList);
    }
}
