package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementDatabaseService databaseService;
}
