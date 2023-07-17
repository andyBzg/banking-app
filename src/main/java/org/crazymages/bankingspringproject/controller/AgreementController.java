package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.dto.AgreementDto;
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
     * @param agreementDto The agreement to create.
     * @return The created agreement.
     */
    @PostMapping(value = "/agreement/create")
    public ResponseEntity<AgreementDto> createAgreement(@RequestBody AgreementDto agreementDto) {
        agreementDatabaseService.create(agreementDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(agreementDto);
    }

    /**
     * Retrieves all agreements.
     *
     * @return The list of agreements.
     */
    @GetMapping(value = "/agreement/find/all")
    public ResponseEntity<List<AgreementDto>> findAllAgreements() {
        List<AgreementDto> agreementDtoList = agreementDatabaseService.findAllNotDeleted();
        return agreementDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementDtoList);
    }

    /**
     * Retrieves an agreement by its UUID.
     *
     * @param uuid The UUID of the agreement.
     * @return The agreement.
     */
    @GetMapping(value = "/agreement/find/{uuid}")
    public ResponseEntity<AgreementDto> findAgreementByUuid(@PathVariable UUID uuid) {
        AgreementDto agreementDto = agreementDatabaseService.findById(uuid);
        return ResponseEntity.ok(agreementDto);
    }

    /**
     * Updates an existing agreement.
     *
     * @param uuid            The UUID of the agreement to update.
     * @param updatedAgreementDto The updated agreement.
     * @return The updated agreement.
     */
    @PutMapping(value = "/agreement/update/{uuid}")
    public ResponseEntity<AgreementDto> updateAgreement(
            @PathVariable UUID uuid, @RequestBody AgreementDto updatedAgreementDto) {
        agreementDatabaseService.update(uuid, updatedAgreementDto);
        return ResponseEntity.ok(updatedAgreementDto);
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
    public ResponseEntity<List<AgreementDto>> findAgreementsByManagerId(@PathVariable UUID uuid) {
        List<AgreementDto> agreementDtoList = agreementDatabaseService.findAgreementsByManagerUuid(uuid);
        return agreementDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementDtoList);
    }

    /**
     * Retrieves all agreements by client UUID.
     *
     * @param uuid The UUID of the client.
     * @return The list of agreements.
     */
    @GetMapping(value = "/agreement/find/all-by-client/{uuid}")
    public ResponseEntity<List<AgreementDto>> findAgreementsByClientId(@PathVariable UUID uuid) {
        List<AgreementDto> agreementDtoList = agreementDatabaseService.findAgreementDtoListByClientUuid(uuid);
        return agreementDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementDtoList);
    }
}
