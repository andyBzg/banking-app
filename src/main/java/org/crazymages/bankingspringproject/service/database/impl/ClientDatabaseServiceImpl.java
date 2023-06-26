package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ClientRepository;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientDatabaseServiceImpl implements ClientDatabaseService {

    private final ClientRepository clientRepository;
    private final EntityUpdateService<Client> clientUpdateService;

    @Override
    public void create(Client client) {
        clientRepository.save(client);
        log.info("client created");
    }

    @Override
    public List<Client> findAll() {
        log.info("retrieving list of clients");
        return clientRepository.findAll();
    }

    @Override
    public Client findById(UUID uuid) {
        log.info("retrieving client by id {}", uuid);
        return clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public void update(UUID uuid, Client clientUpdate) {
        Client client = clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        client = clientUpdateService.update(client, clientUpdate);
        clientRepository.save(client);
        log.info("updated client id {}", uuid);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        Client client = clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        client.setDeleted(true);
        clientRepository.save(client);
        log.info("deleted client id {}", uuid);
    }

    @Override
    @Transactional
    public List<Client> findActiveClients() {
        log.info("retrieving list of active clients");
        return clientRepository.findClientsByStatusIs(ClientStatus.ACTIVE);
    }

    @Override
    @Transactional
    public List<Client> findClientsWhereBalanceMoreThan(BigDecimal balance) {
        log.info("retrieving list of clients where balance is more than {}", balance);
        return clientRepository.findAllClientsWhereBalanceMoreThan(balance);
    }

    @Override
    @Transactional
    public List<Client> findClientsWhereTransactionMoreThan(Integer count) {
        log.info("retrieving list of clients where transaction count is more than {}", count);
        return clientRepository.findAllClientsWhereTransactionMoreThan(count);
    }

    @Override
    @Transactional
    public BigDecimal calculateTotalBalanceByClientUuid(UUID uuid) {
        log.info("calculating total balance for client id {}", uuid);
        return clientRepository.calculateTotalBalanceByClientUuid(uuid);
    }

    @Override
    @Transactional
    public boolean isClientStatusActive(UUID uuid) {
        log.info("checking status for client id {}", uuid);
        return clientRepository.isClientStatusBlocked(uuid);
    }
}
