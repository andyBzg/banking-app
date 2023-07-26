package org.crazymages.bankingspringproject.config;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.entity.enums.Roles;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.dto.mapper.client.ClientDtoMapper;
import org.crazymages.bankingspringproject.dto.mapper.manager.ManagerDtoMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for web security.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final ClientDatabaseService clientDatabaseService;
    private final ManagerDatabaseService managerDatabaseService;
    private final ClientDtoMapper clientDtoMapper;
    private final ManagerDtoMapper managerDtoMapper;


    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        List<UserDetails> users = new ArrayList<>();

        List<Client> clients = clientDatabaseService.findAllNotDeleted()
                .stream()
                .map(clientDtoMapper::mapDtoToEntity)
                .toList();

        for (Client client : clients) {
            UserDetails userDetails = User.withDefaultPasswordEncoder()
                    .username(client.getEmail())
                    .password("user")
                    .roles(Roles.USER.toString())
                    .build();
            users.add(userDetails);
        }

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("root")
                .roles(Roles.ADMIN.name(), Roles.MANAGER.name(), Roles.USER.name())
                .build();
        users.add(admin);

        List<Manager> managers = managerDatabaseService.findAllNotDeleted()
                .stream()
                .map(managerDtoMapper::mapDtoToEntity)
                .filter(m -> m.getStatus().equals(ManagerStatus.ACTIVE))
                .toList();

        for (Manager manager : managers) {
            UserDetails userDetails = User.withDefaultPasswordEncoder()
                    .username(manager.getLastName())
                    .password("manager")
                    .roles(Roles.MANAGER.name(), Roles.USER.name())
                    .build();
            users.add(userDetails);
        }

        return new InMemoryUserDetailsManager(users);
    }

    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity object
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/v3/api-docs/**").permitAll();
                    auth.requestMatchers("/swagger-ui/**").permitAll();
                    auth.requestMatchers("/swagger-ui.html").permitAll();

                    auth.requestMatchers("/exchange/get-rates").permitAll();

                    auth.requestMatchers("/account/create/with-client-id/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/account/find/all/by-client-id/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/agreement/find/all-by-client-id/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/client/find/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/client/create").hasRole(Roles.USER.name());
                    auth.requestMatchers("/client/update/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/client/total-balance-of-accounts/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/product/find/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/product/find/all").hasRole(Roles.USER.name());
                    auth.requestMatchers("/transaction/get/client/{uuid}/**").hasRole(Roles.USER.name());
                    auth.requestMatchers("/transaction/create").hasRole(Roles.USER.name());
                    auth.requestMatchers("/transaction/find/**").hasRole(Roles.USER.name());
                    auth.requestMatchers("/transaction/transfer/").hasRole(Roles.USER.name());
                    auth.requestMatchers("/manager/find/{uuid}").hasRole(Roles.MANAGER.name());

                    auth.requestMatchers("/account/**").hasRole(Roles.MANAGER.name());
                    auth.requestMatchers("/agreement/**").hasRole(Roles.MANAGER.name());
                    auth.requestMatchers("/client/**").hasRole(Roles.MANAGER.name());
                    auth.requestMatchers("/product/**").hasRole(Roles.MANAGER.name());
                    auth.requestMatchers("/transaction/**").hasRole(Roles.MANAGER.name());

                    auth.requestMatchers("/**").hasRole(Roles.ADMIN.name());
                })
                .formLogin(formLogin -> formLogin.defaultSuccessUrl("/swagger-ui.html"))
                .logout(Customizer.withDefaults())
                .build();
    }
}
