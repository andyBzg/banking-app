package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementDatabaseService agreementDatabaseService;


    @PostMapping(value = "/agreement/create")
    public ResponseEntity<Agreement> createAgreement(@RequestBody Agreement agreement) {
        agreementDatabaseService.create(agreement);
        return ResponseEntity.status(HttpStatus.CREATED).body(agreement);
    }

    @GetMapping(value = "/agreement/find/all")
    public ResponseEntity<List<Agreement>> findAllAgreements() {
        List<Agreement> agreementList = agreementDatabaseService.findAll();
        return createResponseEntity(agreementList);
    }

    @GetMapping(value = "/agreement/find/{uuid}")
    public ResponseEntity<Agreement> findAgreementByUuid(@PathVariable UUID uuid) {
        Agreement agreement = agreementDatabaseService.findById(uuid);
        return ResponseEntity.ok(agreement);
    }

    @PutMapping(value = "/agreement/update/{uuid}")
    public ResponseEntity<Agreement> updateAgreement(@PathVariable UUID uuid, @RequestBody Agreement udatedAgreement) {
        agreementDatabaseService.update(uuid, udatedAgreement);
        return ResponseEntity.ok(udatedAgreement);
    }

    @DeleteMapping(value ="/agreement/delete/{uuid}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID uuid) {
        agreementDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }

    //TODO лог, тесты, postman
    @GetMapping(value = "/agreement/find/all-by-manager/{uuid}")
    public ResponseEntity<List<Agreement>> findAgreementsByManagerId(@PathVariable UUID uuid) {
        List<Agreement> agreementList = agreementDatabaseService.findAgreementsByManagerUuid(uuid);
        return createResponseEntity(agreementList);
    }

    //TODO лог, тесты, postman
    @GetMapping(value = "/agreement/find/all-by-client/{uuid}")
    public ResponseEntity<List<Agreement>> findAgreementsByClientId(@PathVariable UUID uuid) {
        List<Agreement> agreementList = agreementDatabaseService.findAgreementsByClientUuid(uuid);
        return createResponseEntity(agreementList);
    }

    private ResponseEntity<List<Agreement>> createResponseEntity(List<Agreement> agreementList) {
        if (agreementList != null && !agreementList.isEmpty()) {
            return ResponseEntity.ok(agreementList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
