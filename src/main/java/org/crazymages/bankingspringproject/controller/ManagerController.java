package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerDatabaseService databaseService;
}
