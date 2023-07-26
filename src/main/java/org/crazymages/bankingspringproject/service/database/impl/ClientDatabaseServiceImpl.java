package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.ClientDto;
import org.crazymages.bankingspringproject.dto.mapper.client.ClientCreationMapper;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ClientRepository;
import org.crazymages.bankingspringproject.service.database.AccountDatabaseService;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.dto.mapper.client.ClientDtoMapper;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * A service implementation for managing Client entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientDatabaseServiceImpl implements ClientDatabaseService {

    private final ClientRepository clientRepository;
    private final ClientDtoMapper clientDtoMapper;
    private final ClientCreationMapper clientCreationMapper;
    private final EntityUpdateService<Client> clientUpdateService;
    private final ManagerDatabaseService managerDatabaseService;
    private final AccountDatabaseService accountDatabaseService;


    @Override
    @Transactional
    public void create(ClientDto clientDTO) {
        log.info("creating client");
        Client client = clientCreationMapper.mapDtoToEntity(clientDTO);
        client.setStatus(ClientStatus.ACTIVE);
        if (client.getManagerUuid() == null) {
            List<Manager> activeManagers = managerDatabaseService
                    .findManagersSortedByClientQuantityWhereManagerStatusIs(ManagerStatus.ACTIVE);
            Manager firstManager = managerDatabaseService.getFirstManager(activeManagers);
            client.setManagerUuid(firstManager.getUuid());
        }
        clientRepository.save(client);
        log.info("client created");
    }

    @Override
    @Transactional
    public List<ClientDto> findAll() {
        log.info("retrieving list of clients");
        List<Client> clients = clientRepository.findAll();
        return getDtoList(clients);
    }

    @Override
    @Transactional
    public List<ClientDto> findAllNotDeleted() {
        log.info("retrieving list of clients");
        List<Client> clients = clientRepository.findAllNotDeleted();
        return getDtoList(clients);
    }

    @Override
    @Transactional
    public List<ClientDto> findDeletedClients() {
        log.info("retrieving list of deleted agreements");
        List<Client> clients = clientRepository.findAllDeleted();
        return getDtoList(clients);
    }

    @Override
    @Transactional
    public ClientDto findById(String clientUuid) {
        log.info("retrieving client by id {}", clientUuid);
        if (clientUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(clientUuid);
        return clientDtoMapper.mapEntityToDto(
                clientRepository.findById(uuid)
                        .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public void update(String clientUuid, ClientDto updatedClientDto) {
        if (clientUuid == null || updatedClientDto == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(clientUuid);
        Client updatedClient = clientCreationMapper.mapDtoToEntity(updatedClientDto);
        Client client = clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        client = clientUpdateService.update(client, updatedClient);
        clientRepository.save(client);
        log.info("updated client id {}", uuid);
    }

    @Override
    @Transactional
    public void delete(String clientUuid) {
        if (clientUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(clientUuid);
        Client client = clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        client.setDeleted(true);
        clientRepository.save(client);
        log.info("deleted client id {}", uuid);
    }

    @Override
    @Transactional
    public List<ClientDto> findActiveClients() {
        log.info("retrieving list of active clients");
        List<Client> clients = clientRepository.findClientsByStatusIs(ClientStatus.ACTIVE);
        return getDtoList(clients);
    }

    @Override
    @Transactional
    public List<ClientDto> findClientsWhereBalanceMoreThan(BigDecimal balance) {
        log.info("retrieving list of clients where balance is more than {}", balance);
        List<Client> clients = clientRepository.findAllClientsWhereBalanceMoreThan(balance);
        return getDtoList(clients);
    }

    @Override
    @Transactional
    public List<ClientDto> findClientsWhereTransactionMoreThan(Integer count) {
        log.info("retrieving list of clients where transaction count is more than {}", count);
        List<Client> clients = clientRepository.findAllClientsWhereTransactionMoreThan(count);
        return getDtoList(clients);
    }

    @Override
    @Transactional
    public BigDecimal calculateTotalBalanceByClientUuid(String clientUuid) {
        if (clientUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(clientUuid);
        log.info("calculating total balance for client id {}", uuid);
        return clientRepository.calculateTotalBalanceByClientUuid(uuid);
    }

    @Override
    @Transactional
    public boolean isClientStatusActive(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException();
        }
        log.info("checking status for client id {}", uuid);
        return clientRepository.isClientStatusBlocked(uuid);
    }

    @Override
    @Transactional
    public List<Client> findClientsWithCurrentAndSavingsAccounts() {
        log.info("retrieving clients where status is {} and {}", AccountType.CURRENT, AccountType.SAVINGS);
        List<Client> clients = clientRepository.findAllActiveClientsWithTwoDifferentAccountTypes(
                AccountType.CURRENT, AccountType.SAVINGS);
        return Optional.of(clients)
                .orElse(Collections.emptyList())
                .stream()
                .toList();
    }

    @Override
    @Transactional
    public List<Client> findClientsByStatus(ClientStatus status) {
        log.info("retrieving clients where status is {}", status);
        return clientRepository.findClientsByStatusIs(status);
    }

    @Override
    @Transactional
    public void blockClientById(String clientUuid) {
        if (clientUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(clientUuid);
        log.info("blocked client with uuid {}", uuid);
        clientRepository.blockClientById(uuid);
        accountDatabaseService.blockAccountsByClientUuid(clientUuid);
    }

    private List<ClientDto> getDtoList(List<Client> clients) {
        return Optional.ofNullable(clients)
                .orElse(Collections.emptyList())
                .stream()
                .map(clientDtoMapper::mapEntityToDto)
                .toList();
    }
}
