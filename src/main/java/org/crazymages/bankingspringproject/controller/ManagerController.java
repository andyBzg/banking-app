package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerDatabaseService managerDatabaseService;


    @PostMapping(value = "/manager/create")
    public ResponseEntity<Manager> createManager(@RequestBody Manager manager) {
        managerDatabaseService.create(manager);
        return ResponseEntity.status(HttpStatus.CREATED).body(manager);
    }

    @GetMapping(value = "/manager/find/all")
    public ResponseEntity<List<Manager>> findAllManagers() {
        List<Manager> managerList = managerDatabaseService.findAllNotDeleted();
        return managerList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(managerList);
    }

    @GetMapping(value = "/manager/find/{uuid}")
    public ResponseEntity<Manager> findManagerByUuid(@PathVariable UUID uuid) {
        Manager manager = managerDatabaseService.findById(uuid);
        return ResponseEntity.ok(manager);
    }

    @PutMapping(value = "/manager/update/{uuid}")
    public ResponseEntity<Manager> updateManager(@PathVariable UUID uuid, @RequestBody Manager updatedManager) {
        managerDatabaseService.update(uuid, updatedManager);
        return ResponseEntity.ok(updatedManager);
    }

    @DeleteMapping(value ="/manager/delete/{uuid}")
    public ResponseEntity<String> deleteManager(@PathVariable UUID uuid) {
        managerDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
