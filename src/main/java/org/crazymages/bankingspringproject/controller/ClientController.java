package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.ClientDto;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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
     * @param clientDto The client to create.
     * @return The created client.
     */
    @PostMapping(value = "/client/create")
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        log.info("endpoint request: create client");
        clientDatabaseService.create(clientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientDto);
    }

    /**
     * Retrieves all clients.
     *
     * @return The list of clients.
     */
    @GetMapping(value = "/client/find-all")
    public ResponseEntity<List<ClientDto>> findAllClients() {
        List<ClientDto> clientDtoList = clientDatabaseService.findAllNotDeleted();
        return clientDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientDtoList);
    }

    /**
     * Retrieves a client by its UUID.
     *
     * @param uuid The UUID of the client.
     * @return The client.
     */
    @GetMapping(value = "/client/find/{uuid}")
    public ResponseEntity<ClientDto> findClientByUuid(@PathVariable String uuid) {
        ClientDto clientDto = clientDatabaseService.findById(uuid);
        return ResponseEntity.ok(clientDto);
    }

    /**
     * Updates an existing client.
     *
     * @param uuid             The UUID of the client to update.
     * @param updatedClientDto The updated client.
     * @return The updated client.
     */
    @PutMapping(value = "/client/update/{uuid}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable String uuid, @RequestBody ClientDto updatedClientDto) {
        clientDatabaseService.update(uuid, updatedClientDto);
        return ResponseEntity.ok(updatedClientDto);
    }

    /**
     * Deletes a client by its UUID.
     *
     * @param uuid The UUID of the client to delete.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/client/delete/{uuid}")
    public ResponseEntity<String> deleteClient(@PathVariable String uuid) {
        clientDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all active clients.
     *
     * @return The list of active clients.
     */
    @GetMapping(value = "/client/find-all/active-clients")
    public ResponseEntity<List<ClientDto>> findActiveClients() {
        List<ClientDto> clientDtoList = clientDatabaseService.findActiveClients();
        return clientDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientDtoList);
    }

    /**
     * Retrieves clients where the balance is more than the specified amount.
     *
     * @param balance The minimum balance amount.
     * @return The list of clients.
     */
    @GetMapping(value = "/client/find-balance/more-than/{balance}")
    public ResponseEntity<List<ClientDto>> findClientsWhereBalanceMoreThan(@PathVariable BigDecimal balance) {
        log.info("endpoint request: find all clients where balance more than {}", balance);
        List<ClientDto> clientDtoList = clientDatabaseService.findClientsWhereBalanceMoreThan(balance);
        return clientDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientDtoList);
    }

    /**
     * Retrieves clients where the transaction count is more than the specified value.
     *
     * @param count The minimum transaction count.
     * @return The list of clients.
     */
    @GetMapping(value = "/client/find-transactions/more-than/{count}")
    public ResponseEntity<List<ClientDto>> findClientsWhereTransactionMoreThan(@PathVariable Integer count) {
        log.info("endpoint request: find all clients where transaction count more than {}", count);
        List<ClientDto> clientDtoList = clientDatabaseService.findClientsWhereTransactionMoreThan(count);
        return clientDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientDtoList);
    }

    /**
     * Calculates the total balance for a client based on its UUID.
     *
     * @param uuid The UUID of the client.
     * @return The total balance.
     */
    @GetMapping(value = "/client/total-balance-of-accounts/{uuid}")
    public ResponseEntity<BigDecimal> calculateTotalBalanceByClientUuid(@PathVariable String uuid) {
        log.info("endpoint request: get total balance of client accounts");
        BigDecimal result = clientDatabaseService.calculateTotalBalanceByClientUuid(uuid);
        return ResponseEntity.ok(result);
    }
}
