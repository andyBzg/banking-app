package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ClientDatabaseService clientDatabaseService;


    @PostMapping(value = "/client/create")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        clientDatabaseService.create(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @GetMapping(value = "/client/find/all")
    public ResponseEntity<List<Client>> findAllClients() {
        List<Client> clientList = clientDatabaseService.findAll();
        if (clientList != null && !clientList.isEmpty()) {
            return ResponseEntity.ok(clientList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/client/find/{uuid}")
    public ResponseEntity<Client> findClientByUuid(@PathVariable UUID uuid) {
        Client client = clientDatabaseService.findById(uuid);
        return ResponseEntity.ok(client);
    }

    @PutMapping(value = "/client/update/{uuid}")
    public ResponseEntity<Client> updateClient(@PathVariable UUID uuid, @RequestBody Client client) {
        Client clientUpdate = clientDatabaseService.update(uuid, client);
        return ResponseEntity.ok(clientUpdate);
    }

    @DeleteMapping(value ="/client/delete/{uuid}")
    public ResponseEntity<String> deleteClient(@PathVariable UUID uuid) {
        clientDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
