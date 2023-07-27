package org.crazymages.bankingspringproject.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.Roles;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UsersConfig {

    private final JdbcUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final ManagerDatabaseService managerDatabaseService;

    @Value("${admin.username}")
    private String adminUsername;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${manager.password}")
    private String managerPassword;

    @PostConstruct
    public void createUsersAndAuthorities() {
        if (!userDetailsManager.userExists(adminUsername)) {
            String encodedPassword = passwordEncoder.encode(adminPassword);
            userDetailsManager.createUser(User.withUsername(adminUsername)
                    .password(encodedPassword)
                    .roles(Roles.ADMIN.name(), Roles.MANAGER.name(), Roles.USER.name())
                    .build());
        }

        List<Manager> managers = managerDatabaseService.findAll()
                .stream()
                .filter(m -> !m.isDeleted())
                .toList();
        for (Manager manager : managers) {
            if (!userDetailsManager.userExists(manager.getLastName())) {
                String encodedManagerPassword = passwordEncoder.encode(managerPassword);
                userDetailsManager.createUser(User.withUsername(manager.getLastName())
                        .password(encodedManagerPassword)
                        .roles(Roles.MANAGER.name(), Roles.USER.name())
                        .build());
            }
        }
    }
}
