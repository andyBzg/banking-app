package org.crazymages.bankingspringproject.service.database.updater.impl;

import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.service.database.updater.EntityUpdateService;
import org.springframework.stereotype.Service;

@Service
public class ClientUpdateServiceImpl implements EntityUpdateService<Client> {
    @Override
    public Client update(Client client, Client clientUpdate) {
        if (clientUpdate.getManagerUuid() != null) {
            client.setManagerUuid(clientUpdate.getManagerUuid());
        }
        if (clientUpdate.getStatus() != null) {
            client.setStatus(clientUpdate.getStatus());
        }
        if (clientUpdate.getTaxCode() != null) {
            client.setTaxCode(clientUpdate.getTaxCode());
        }
        if (clientUpdate.getFirstName() != null) {
            client.setFirstName(clientUpdate.getFirstName());
        }
        if (clientUpdate.getLastName() != null) {
            client.setLastName(clientUpdate.getLastName());
        }
        if (clientUpdate.getEmail() != null) {
            client.setEmail(clientUpdate.getEmail());
        }
        if (clientUpdate.getAddress() != null) {
            client.setAddress(clientUpdate.getAddress());
        }
        if (clientUpdate.getPhone() != null) {
            client.setPhone(clientUpdate.getPhone());
        }
        return client;
    }
}
