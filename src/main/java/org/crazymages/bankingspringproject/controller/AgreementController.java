package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AgreementDto;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for managing agreements.
 */
@RestController
@RequestMapping("/api/agreement")
@RequiredArgsConstructor
@Slf4j
public class AgreementController {

    private final AgreementDatabaseService agreementDatabaseService;

    /**
     * Creates a new agreement.
     *
     * @param agreementDto The agreement to create.
     * @return The created agreement.
     */
    @PostMapping(value = "/create")
    public ResponseEntity<AgreementDto> createAgreement(@RequestBody AgreementDto agreementDto) {
        log.info("endpoint request: create agreement");
        agreementDatabaseService.create(agreementDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(agreementDto);
    }

    /**
     * Retrieves all agreements.
     *
     * @return The list of agreements.
     */
    @GetMapping(value = "/find/all")
    public ResponseEntity<List<AgreementDto>> findAllAgreements() {
        log.info("endpoint request: find all agreements");
        List<AgreementDto> agreementDtoList = agreementDatabaseService.findAllNotDeleted();
        return agreementDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementDtoList);
    }

    /**
     * Retrieves an agreement by its UUID.
     *
     * @param uuid The UUID of the agreement.
     * @return The agreement.
     */
    @GetMapping(value = "/find/{uuid}")
    public ResponseEntity<AgreementDto> findAgreementByUuid(@PathVariable String uuid) {
        log.info("endpoint request: find agreement by uuid {}", uuid);
        AgreementDto agreementDto = agreementDatabaseService.findById(uuid);
        return ResponseEntity.ok(agreementDto);
    }

    /**
     * Updates an existing agreement.
     *
     * @param uuid                The UUID of the agreement to update.
     * @param updatedAgreementDto The updated agreement.
     * @return The updated agreement.
     */
    @PutMapping(value = "/update/{uuid}")
    public ResponseEntity<AgreementDto> updateAgreement(
            @PathVariable String uuid, @RequestBody AgreementDto updatedAgreementDto) {
        log.info("endpoint request: update agreement uuid {}", uuid);
        agreementDatabaseService.update(uuid, updatedAgreementDto);
        return ResponseEntity.ok(updatedAgreementDto);
    }

    /**
     * Deletes an agreement by its UUID.
     *
     * @param uuid The UUID of the agreement to delete.
     * @return A response indicating the success of the operation.
     */
    @DeleteMapping(value = "/delete/{uuid}")
    public ResponseEntity<String> deleteAccount(@PathVariable String uuid) {
        log.info("endpoint request: delete agreement uuid {}", uuid);
        agreementDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves all agreements by manager UUID.
     *
     * @param uuid The UUID of the manager.
     * @return The list of agreements.
     */
    @GetMapping(value = "/find/all-by-manager-id/{uuid}")
    public ResponseEntity<List<AgreementDto>> findAgreementsByManagerId(@PathVariable String uuid) {
        log.info("endpoint request: find agreement by manager uuid {}", uuid);
        List<AgreementDto> agreementDtoList = agreementDatabaseService.findAgreementsByManagerUuid(uuid);
        return agreementDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementDtoList);
    }

    /**
     * Retrieves all agreements by client UUID.
     *
     * @param uuid The UUID of the client.
     * @return The list of agreements.
     */
    @GetMapping(value = "/find/all-by-client-id/{uuid}")
    public ResponseEntity<List<AgreementDto>> findAgreementsByClientId(@PathVariable String uuid) {
        log.info("endpoint request: find agreement by client uuid {}", uuid);
        List<AgreementDto> agreementDtoList = agreementDatabaseService.findAgreementDtoListByClientUuid(uuid);
        return agreementDtoList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(agreementDtoList);
    }
}
