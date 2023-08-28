package org.crazymages.bankingspringproject.service.registration;

import org.crazymages.bankingspringproject.dto.client.ClientRegistrationDto;

public interface ClientRegistrationService {
    void registerNewClient(ClientRegistrationDto clientRegistrationDto);
}
