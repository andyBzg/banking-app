package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller class for managing agreements.
 */
@RestController
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementDatabaseService agreementDatabaseService;

    /**
     * Creates a new agreement.
     *
     * @param agreement The agreement to create.
     * @return The created agreement.
     */
    @PostMapping(value = "/agreement/create")
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) {
        agreementDatabaseService.create(agreement);
        return ResponseEntity.status(HttpStatus.CREATED).body(agreement);
    }

    /**
     * Retrieves all agreements.
     *
     * @return The list of agreements.
     */
    @GetMapping(value = "/agreement/find/all")
    public ResponseEntity<List<Agreement>> findAllAgreements() {
        List<Agreement> agreementList = agreementDatabaseService.findAllNotDeleted();
        return createResponseEntity(agreementList);
    }

    /**
     * Retrieves an agreement by its UUID.
     *
     * @param uuid The UUID of the agreement.
     * @return The agreement.
     */
    @GetMapping(value = "/agreement/find/{uuid}")
    public ResponseEntity<Agreement> findAgreementByUuid(@PathVariable UUID uuid) {
        Agreement agreement = agreementDatabaseService.findById(uuid);
        return ResponseEntity.ok(agreement);
    }

    /**
     * Updates an existing agreement.
     *
     * @param uuid            The UUID of the agreement to update.
     * @param updatedAgreement The updated agreement.
     * @return The updated agreement.
     */
    @PutMapping(value = "/agreement/update/{uuid}")
    public ResponseEntity<Agreement> updateAgreement(@PathVariable UUID uuid, @RequestBody Agreement updatedAgreement) {
        agreementDatabaseService.update(uuid, updatedAgreement);
        return ResponseEntity.ok(updatedAgreement);
    }

    /**
     * Deletes an agreement by its UUID.
     *
     * @param uuid The UUID of the agreement to delete.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/agreement/delete/{uuid}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID uuid) {
        agreementDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all agreements by manager UUID.
     *
     * @param uuid The UUID of the manager.
     * @return The list of agreements.
     */
    @GetMapping(value = "/agreement/find/all-by-manager/{uuid}")
    public ResponseEntity<List<Agreement>> findAgreementsByManagerId(@PathVariable UUID uuid) {
        List<Agreement> agreementList = agreementDatabaseService.findAgreementsByManagerUuid(uuid);
        return createResponseEntity(agreementList);
    }

    /**
     * Retrieves all agreements by client UUID.
     *
     * @param uuid The UUID of the client.
     * @return The list of agreements.
     */
    @GetMapping(value = "/agreement/find/all-by-client/{uuid}")
    public ResponseEntity<List<Agreement>> findAgreementsByClientId(@PathVariable UUID uuid) {
        List<Agreement> agreementList = agreementDatabaseService.findAgreementsByClientUuid(uuid);
        return createResponseEntity(agreementList);
    }

    /**
     * Creates a ResponseEntity based on the given agreement list.
     * If the agreement list is empty, return a no-content response.
     * Otherwise, return a response with the agreement list.
     *
     * @param agreementList The list of agreements.
     * @return The ResponseEntity.
     */
    private ResponseEntity<List<Agreement>> createResponseEntity(List<Agreement> agreementList) {
        return agreementList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementList);
    }
}
