package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.RegistrationDto;
import org.crazymages.bankingspringproject.service.registration.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(value = "/registration")
    public ResponseEntity<RegistrationDto> createClient(@RequestBody RegistrationDto registrationDto) {
        log.info("endpoint request: new client registration");
        registrationService.registerNewClient(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationDto);
    }
}
