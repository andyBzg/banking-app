package org.crazymages.bankingspringproject.service.registration;

import org.crazymages.bankingspringproject.dto.RegistrationDto;

public interface RegistrationService {
    void registerNewClient(RegistrationDto registrationDto);
}
