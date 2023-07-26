package org.crazymages.bankingspringproject.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogoutController {

    @PostMapping("logout")
    public void logout() {
        // Fake REST Controller exposing a post-request mapping for the /logout path.
        // This will make logout available in Swagger-UI.
    }
}
