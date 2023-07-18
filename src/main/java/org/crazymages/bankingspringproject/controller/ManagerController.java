package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.dto.ManagerDto;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param managerDto The manager to create.
     * @return The created manager.
     */
    @PostMapping(value = "/manager/create")
    public ResponseEntity<ManagerDto> createManager(@RequestBody ManagerDto managerDto) {
        managerDatabaseService.create(managerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(managerDto);
    }

    /**
     * Retrieves all managers.
     *
     * @return The list of managers.
     */
    @GetMapping(value = "/manager/find/all")
    public ResponseEntity<List<ManagerDto>> findAllManagers() {
        List<ManagerDto> managerDtoList = managerDatabaseService.findAllNotDeleted();
        return managerDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(managerDtoList);
    }

    /**
     * Retrieves a manager by its UUID.
     *
     * @param uuid The UUID of the manager.
     * @return The manager.
     */
    @GetMapping(value = "/manager/find/{uuid}")
    public ResponseEntity<ManagerDto> findManagerByUuid(@PathVariable String uuid) {
        ManagerDto managerDto = managerDatabaseService.findById(uuid);
        return ResponseEntity.ok(managerDto);
    }

    /**
     * Updates an existing manager.
     *
     * @param uuid              The UUID of the manager to update.
     * @param updatedManagerDto The updated manager.
     * @return The updated manager.
     */
    @PutMapping(value = "/manager/update/{uuid}")
    public ResponseEntity<ManagerDto> updateManager(
            @PathVariable String uuid, @RequestBody ManagerDto updatedManagerDto) {
        managerDatabaseService.update(uuid, updatedManagerDto);
        return ResponseEntity.ok(updatedManagerDto);
    }

    /**
     * Deletes a manager by its UUID.
     *
     * @param uuid The UUID of the manager to delete.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/manager/delete/{uuid}")
    public ResponseEntity<String> deleteManager(@PathVariable String uuid) {
        managerDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
