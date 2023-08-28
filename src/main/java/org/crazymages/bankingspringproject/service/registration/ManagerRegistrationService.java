package org.crazymages.bankingspringproject.service.registration;

import org.crazymages.bankingspringproject.dto.manager.ManagerRegistrationDto;

public interface ManagerRegistrationService {
    void registerNewManager(ManagerRegistrationDto registrationDto);
}
