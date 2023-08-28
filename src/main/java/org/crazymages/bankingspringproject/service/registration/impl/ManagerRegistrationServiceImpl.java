package org.crazymages.bankingspringproject.service.registration.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.manager.ManagerRegistrationDto;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.entity.enums.Roles;
import org.crazymages.bankingspringproject.exception.UserAlreadyExistsException;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.service.registration.ManagerRegistrationService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerRegistrationServiceImpl implements ManagerRegistrationService {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final ManagerDatabaseService managerDatabaseService;

    @Override
    public void registerNewManager(ManagerRegistrationDto registrationDto) {
        String login = registrationDto.getLogin();
        String encodedPassword = passwordEncoder.encode(registrationDto.getPassword());

        if (userDetailsManager.userExists(login)) {
            throw new UserAlreadyExistsException("Unable to register username, already exists in DB");
        }
        saveManagerCredentials(login, encodedPassword);

        Manager manager = initializeNewManagerInstance(registrationDto);
        managerDatabaseService.save(manager);
        log.info("client created");
    }

    private Manager initializeNewManagerInstance(ManagerRegistrationDto registrationDto) {
        if (registrationDto == null) {
            throw new IllegalArgumentException("registrationDto cannot be null");
        }
        return Manager.builder()
                .firstName(registrationDto.getFirstName())
                .lastName(registrationDto.getLastName())
                .status(ManagerStatus.ACTIVE)
                .description(registrationDto.getDescription())
                .build();
    }

    private void saveManagerCredentials(String login, String encodedPassword) {
        userDetailsManager.createUser(User.withUsername(login)
                .password(encodedPassword)
                .roles(Roles.MANAGER.name(), Roles.USER.name())
                .build());
    }
}
