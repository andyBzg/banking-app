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
        if (agreementList != null && !agreementList.isEmpty()) {
            return ResponseEntity.ok(agreementList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(value = "/agreement/find/{uuid}")
    public ResponseEntity<Agreement> findAgreementByUuid(@PathVariable UUID uuid) {
        Agreement agreement = agreementDatabaseService.findById(uuid);
        return ResponseEntity.ok(agreement);
    }

    @PutMapping(value = "/agreement/update/{uuid}")
    public ResponseEntity<Agreement> updateAgreement(@PathVariable UUID uuid, @RequestBody Agreement agreement) {
        Agreement agreementUpdate = agreementDatabaseService.update(uuid, agreement);
        return ResponseEntity.ok(agreementUpdate);
    }

    @DeleteMapping(value ="/agreement/delete/{uuid}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID uuid) {
        agreementDatabaseService.delete(uuid);
        return ResponseEntity.ok().build();
    }
}
