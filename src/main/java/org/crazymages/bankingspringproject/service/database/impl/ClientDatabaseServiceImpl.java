package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.repository.ClientRepository;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientDatabaseServiceImpl implements ClientDatabaseService {

    private final ClientRepository clientRepository;

    @Override
    public void create(Client client) {
        clientRepository.save(client);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client findById(UUID uuid) {
        Optional<Client> clientOptional = clientRepository.findById(uuid);
        return clientOptional.orElse(null);
    }

    @Override
    @Transactional
    public Client update(UUID uuid, Client clientUpdate) {
        Optional<Client> clientOptional = clientRepository.findById(uuid);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            client.setManagerUuid(clientUpdate.getManagerUuid());
            client.setStatus(clientUpdate.getStatus());
            client.setTaxCode(clientUpdate.getTaxCode());
            client.setFirstName(clientUpdate.getFirstName());
            client.setLastName(clientUpdate.getLastName());
            client.setEmail(clientUpdate.getEmail());
            client.setAddress(clientUpdate.getAddress());
            client.setPhone(clientUpdate.getPhone());
            clientRepository.save(client);
        }
        return clientOptional.orElse(null);
    }

    @Override
    public void delete(UUID uuid) {
        clientRepository.deleteById(uuid);
    }
}
