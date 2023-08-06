package org.crazymages.bankingspringproject.config;

import org.crazymages.bankingspringproject.entity.enums.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public JdbcUserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/v3/api-docs/**").permitAll();
                    auth.requestMatchers("/swagger-ui/**").permitAll();
                    auth.requestMatchers("/swagger-ui.html").permitAll();

                    auth.requestMatchers("/exchange/get-rates").permitAll();
                    auth.requestMatchers("/registration/new-client").permitAll();

                    auth.requestMatchers("/account/create/with-client-id/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/account/find/all/by-client-id/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/agreement/find/all-by-client-id/{uuid}").hasRole(Roles.USER.name());
                    auth.requestMatchers("/client/find/{uuid}").hasRole(Roles.USER.name());
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
