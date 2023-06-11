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
        List<Manager> managerList = managerDatabaseService.findAll();
        if (managerList != null && !managerList.isEmpty()) {
            return ResponseEntity.ok(managerList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/manager/find/{uuid}")
    public ResponseEntity<Manager> findManagerByUuid(@PathVariable UUID uuid) {
        Manager manager = managerDatabaseService.findById(uuid);
        return manager != null ? ResponseEntity.ok(manager) : ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/manager/update/{uuid}")
    public ResponseEntity<Manager> updateManager(@PathVariable UUID uuid, @RequestBody Manager manager) {
        Manager managerUpdate = managerDatabaseService.update(uuid, manager);
        return managerUpdate != null ? ResponseEntity.ok(managerUpdate) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value ="/manager/delete/{uuid}")
    public ResponseEntity<String> deleteManager(@PathVariable UUID uuid) {
        managerDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
