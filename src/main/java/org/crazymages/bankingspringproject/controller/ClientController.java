package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Controller class for managing clients.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientDatabaseService clientDatabaseService;

    /**
     * Creates a new client.
     *
     * @param client The client to create.
     * @return The created client.
     */
    @PostMapping(value = "/client/create")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        clientDatabaseService.create(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    /**
     * Retrieves all clients.
     *
     * @return The list of clients.
     */
    @GetMapping(value = "/client/find/all")
    public ResponseEntity<List<Client>> findAllClients() {
        List<Client> clientList = clientDatabaseService.findAllNotDeleted();
        return createResponseEntity(clientList);
    }

    /**
     * Retrieves a client by its UUID.
     *
     * @param uuid The UUID of the client.
     * @return The client.
     */
    @GetMapping(value = "/client/find/{uuid}")
    public ResponseEntity<Client> findClientByUuid(@PathVariable UUID uuid) {
        Client client = clientDatabaseService.findById(uuid);
        return ResponseEntity.ok(client);
    }

    /**
     * Updates an existing client.
     *
     * @param uuid          The UUID of the client to update.
     * @param updatedClient The updated client.
     * @return The updated client.
     */
    @PutMapping(value = "/client/update/{uuid}")
    public ResponseEntity<Client> updateClient(@PathVariable UUID uuid, @RequestBody Client updatedClient) {
        clientDatabaseService.update(uuid, updatedClient);
        return ResponseEntity.ok(updatedClient);
    }

    /**
     * Deletes a client by its UUID.
     *
     * @param uuid The UUID of the client to delete.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/client/delete/{uuid}")
    public ResponseEntity<String> deleteClient(@PathVariable UUID uuid) {
        clientDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all active clients.
     *
     * @return The list of active clients.
     */
    @GetMapping(value = "/client/find/active-clients")
    public ResponseEntity<List<Client>> findActiveClients() {
        List<Client> clientList = clientDatabaseService.findActiveClients();
        return createResponseEntity(clientList);
    }

    /**
     * Retrieves clients where the balance is more than the specified amount.
     *
     * @param balance The minimum balance amount.
     * @return The list of clients.
     */
    @GetMapping(value = "/client/find/balance-more-than/{balance}")
    public ResponseEntity<List<Client>> findClientsWhereBalanceMoreThan(@PathVariable BigDecimal balance) {
        log.info("endpoint request: find all clients where balance more than {}", balance);
        List<Client> clientList = clientDatabaseService.findClientsWhereBalanceMoreThan(balance);
        return createResponseEntity(clientList);
    }

    /**
     * Retrieves clients where the transaction count is more than the specified value.
     *
     * @param count The minimum transaction count.
     * @return The list of clients.
     */
    @GetMapping(value = "/client/find/transactions-more-than/{count}")
    public ResponseEntity<List<Client>> findClientsWhereTransactionMoreThan(@PathVariable Integer count) {
        log.info("endpoint request: find all clients where transaction count more than {}", count);
        List<Client> clientList = clientDatabaseService.findClientsWhereTransactionMoreThan(count);
        return createResponseEntity(clientList);
    }

    /**
     * Calculates the total balance for a client based on its UUID.
     *
     * @param uuid The UUID of the client.
     * @return The total balance.
     */
    @GetMapping(value = "/client/calculate-total-balance/{uuid}")
    public ResponseEntity<BigDecimal> calculateTotalBalanceByClientUuid(@PathVariable UUID uuid) {
        BigDecimal result = clientDatabaseService.calculateTotalBalanceByClientUuid(uuid);
        return ResponseEntity.ok(result);
    }

    /**
     * Creates a ResponseEntity based on the given client list.
     *
     * @param clientList The list of clients.
     * @return The ResponseEntity.
     */
    private ResponseEntity<List<Client>> createResponseEntity(List<Client> clientList) {
        return clientList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientList);
    }
}
