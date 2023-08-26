package org.crazymages.bankingspringproject.service.registration.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.ClientRegistrationDto;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.entity.enums.Roles;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.exception.UserAlreadyExistsException;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.service.registration.ClientRegistrationService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientRegistrationServiceImpl implements ClientRegistrationService {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final ManagerDatabaseService managerDatabaseService;
    private final ClientDatabaseService clientDatabaseService;

    @Override
    @Transactional
    public void registerNewClient(ClientRegistrationDto clientRegistrationDto) {
        String email = clientRegistrationDto.getEmail();
        String encodedPassword = passwordEncoder.encode(clientRegistrationDto.getPassword());

        if (userDetailsManager.userExists(email)) {
            throw new UserAlreadyExistsException("Unable to register username, already exists in DB");
        }
        saveClientCredentials(email, encodedPassword);

        List<Manager> activeManagers = managerDatabaseService
                .findManagersSortedByClientQuantityWhereManagerStatusIs(ManagerStatus.ACTIVE);
        Manager manager;
        if (!activeManagers.isEmpty()) {
            manager = managerDatabaseService.getFirstManager(activeManagers);
        }
        else {
            manager = managerDatabaseService.findAll()
                    .stream()
                    .filter(mg -> !mg.isDeleted())
                    .filter(mg -> mg.getStatus().equals(ManagerStatus.ACTIVE))
                    .findFirst()
                    .orElseThrow(() -> new DataNotFoundException("Manager not found"));
        }

        Client client = initializeNewClientInstance(clientRegistrationDto);
        client.setManagerUuid(manager.getUuid());
        clientDatabaseService.save(client);
        log.info("client created");
    }

    private Client initializeNewClientInstance(ClientRegistrationDto registration) {
        if (registration == null) {
            throw new IllegalArgumentException("registrationDto cannot be null");
        }
        return Client.builder()
                .status(ClientStatus.ACTIVE)
                .taxCode(registration.getTaxCode())
                .firstName(registration.getFirstName())
                .lastName(registration.getLastName())
                .email(registration.getEmail())
                .address(registration.getAddress())
                .phone(registration.getPhone())
                .build();
    }

    private void saveClientCredentials(String email, String encodedPassword) {
        userDetailsManager.createUser(User.withUsername(email)
                .password(encodedPassword)
                .roles(Roles.USER.name())
                .build());
    }
}
