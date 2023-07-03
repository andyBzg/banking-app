package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for managing managers.
 */
@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerDatabaseService managerDatabaseService;

    /**
     * Creates a new manager.
     *
     * @param manager The manager to create.
     * @return The created manager.
     */
    @PostMapping(value = "/manager/create")
    public ResponseEntity<Manager> createManager(@RequestBody Manager manager) {
        managerDatabaseService.create(manager);
        return ResponseEntity.status(HttpStatus.CREATED).body(manager);
    }

    /**
     * Retrieves all managers.
     *
     * @return The list of managers.
     */
    @GetMapping(value = "/manager/find/all")
    public ResponseEntity<List<Manager>> findAllManagers() {
        List<Manager> managerList = managerDatabaseService.findAllNotDeleted();
        return managerList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(managerList);
    }

    /**
     * Retrieves a manager by its UUID.
     *
     * @param uuid The UUID of the manager.
     * @return The manager.
     */
    @GetMapping(value = "/manager/find/{uuid}")
    public ResponseEntity<Manager> findManagerByUuid(@PathVariable UUID uuid) {
        Manager manager = managerDatabaseService.findById(uuid);
        return ResponseEntity.ok(manager);
    }

    /**
     * Updates an existing manager.
     *
     * @param uuid           The UUID of the manager to update.
     * @param updatedManager The updated manager.
     * @return The updated manager.
     */
    @PutMapping(value = "/manager/update/{uuid}")
    public ResponseEntity<Manager> updateManager(@PathVariable UUID uuid, @RequestBody Manager updatedManager) {
        managerDatabaseService.update(uuid, updatedManager);
        return ResponseEntity.ok(updatedManager);
    }

    /**
     * Deletes a manager by its UUID.
     *
     * @param uuid The UUID of the manager to delete.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/manager/delete/{uuid}")
    public ResponseEntity<String> deleteManager(@PathVariable UUID uuid) {
        managerDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
