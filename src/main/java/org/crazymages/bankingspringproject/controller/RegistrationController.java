package org.crazymages.bankingspringproject.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.ManagerRegistrationDto;
import org.crazymages.bankingspringproject.dto.ClientRegistrationDto;
import org.crazymages.bankingspringproject.service.registration.ClientRegistrationService;
import org.crazymages.bankingspringproject.service.registration.ManagerRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final ClientRegistrationService clientRegistrationService;
    private final ManagerRegistrationService managerRegistrationService;

    @PostMapping(value = "/new-client")
    public ResponseEntity<ClientRegistrationDto> registerClient(@RequestBody ClientRegistrationDto registrationDto) {
        log.info("endpoint request: new client registration");
        clientRegistrationService.registerNewClient(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationDto);
    }

    @PostMapping(value = "/new-manager")
    public ResponseEntity<ManagerRegistrationDto> registerManager(@RequestBody ManagerRegistrationDto registrationDto) {
        log.info("endpoint request: new manager registration");
        managerRegistrationService.registerNewManager(registrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationDto);
    }
}
