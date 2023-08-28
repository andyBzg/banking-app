package org.crazymages.bankingspringproject.service.utils.session;

import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Utility class for retrieving the current username from the authentication context.
 */
@Component
public class CurrentUsernameFinder {

    /**
     * Retrieves the current username from the authentication context.
     *
     * @return The current username.
     * @throws DataNotFoundException If the username is not found in the authentication context.
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        throw new DataNotFoundException("Username is not found");
    }
}
