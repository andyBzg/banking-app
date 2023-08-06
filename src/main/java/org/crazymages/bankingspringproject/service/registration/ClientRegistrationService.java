package org.crazymages.bankingspringproject.service.registration;

import org.crazymages.bankingspringproject.dto.ClientRegistrationDto;

public interface ClientRegistrationService {
    void registerNewClient(ClientRegistrationDto clientRegistrationDto);
}
