package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.dto.AgreementDTO;
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
     * @param agreementDTO The agreement to create.
     * @return The created agreement.
     */
    @PostMapping(value = "/agreement/create")
    public ResponseEntity<AgreementDTO> createAgreement(@RequestBody AgreementDTO agreementDTO) {
        agreementDatabaseService.create(agreementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(agreementDTO);
    }

    /**
     * Retrieves all agreements.
     *
     * @return The list of agreements.
     */
    @GetMapping(value = "/agreement/find/all")
    public ResponseEntity<List<AgreementDTO>> findAllAgreements() {
        List<AgreementDTO> agreementList = agreementDatabaseService.findAllNotDeleted();
        return agreementList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementList);
    }

    /**
     * Retrieves an agreement by its UUID.
     *
     * @param uuid The UUID of the agreement.
     * @return The agreement.
     */
    @GetMapping(value = "/agreement/find/{uuid}")
    public ResponseEntity<AgreementDTO> findAgreementByUuid(@PathVariable UUID uuid) {
        AgreementDTO agreementDTO = agreementDatabaseService.findById(uuid);
        return ResponseEntity.ok(agreementDTO);
    }

    /**
     * Updates an existing agreement.
     *
     * @param uuid            The UUID of the agreement to update.
     * @param updatedAgreementDTO The updated agreement.
     * @return The updated agreement.
     */
    @PutMapping(value = "/agreement/update/{uuid}")
    public ResponseEntity<AgreementDTO> updateAgreement(
            @PathVariable UUID uuid, @RequestBody AgreementDTO updatedAgreementDTO) {
        agreementDatabaseService.update(uuid, updatedAgreementDTO);
        return ResponseEntity.ok(updatedAgreementDTO);
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
    public ResponseEntity<List<AgreementDTO>> findAgreementsByManagerId(@PathVariable UUID uuid) {
        List<AgreementDTO> agreementList = agreementDatabaseService.findAgreementsByManagerUuid(uuid);
        return agreementList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementList);
    }

    /**
     * Retrieves all agreements by client UUID.
     *
     * @param uuid The UUID of the client.
     * @return The list of agreements.
     */
    @GetMapping(value = "/agreement/find/all-by-client/{uuid}")
    public ResponseEntity<List<AgreementDTO>> findAgreementsByClientId(@PathVariable UUID uuid) {
        List<AgreementDTO> agreementList = agreementDatabaseService.findAgreementDTOsByClientUuid(uuid);
        return agreementList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementList);
    }
}
