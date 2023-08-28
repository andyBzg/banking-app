package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.manager.ManagerDto;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for managing managers.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
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
        log.info("endpoint request: create new manager");
        managerDatabaseService.create(managerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(managerDto);
    }

    /**
     * Retrieves all managers.
     *
     * @return The list of managers.
     */
    @GetMapping(value = "/manager/find-all")
    public ResponseEntity<List<ManagerDto>> findAllManagers() {
        log.info("endpoint request: find all managers");
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
        log.info("endpoint request: find manager by id");
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
        log.info("endpoint request: update manager");
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
        log.info("endpoint request: delete manager");
        managerDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
