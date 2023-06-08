package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.service.database.TransactionDatabaseService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionDatabaseService databaseService;
}
