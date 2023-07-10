package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.ClientDTO;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ClientRepository;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.service.utils.mapper.ClientDTOMapper;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A service implementation for managing Client entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientDatabaseServiceImpl implements ClientDatabaseService {

    private final ClientRepository clientRepository;
    private final EntityUpdateService<Client> clientUpdateService;
    private final ManagerDatabaseService managerDatabaseService;
    private final ClientDTOMapper clientDTOMapper;


    @Override
    @Transactional
    public void create(ClientDTO clientDTO) {
        Client client = clientDTOMapper.mapToClient(clientDTO);
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
    public List<ClientDTO> findAll() {
        log.info("retrieving list of clients");
        List<Client> clients = clientRepository.findAll();
        return clientDTOMapper.getListOfAgreementDTOs(clients);
    }

    @Override
    @Transactional
    public List<ClientDTO> findAllNotDeleted() {
        log.info("retrieving list of clients");
        List<Client> clients = clientRepository.findAllNotDeleted();
        return clientDTOMapper.getListOfAgreementDTOs(clients);
    }

    @Override
    @Transactional
    public List<ClientDTO> findDeletedClients() {
        log.info("retrieving list of deleted agreements");
        List<Client> deletedClients = clientRepository.findAllDeleted();
        return clientDTOMapper.getListOfAgreementDTOs(deletedClients);
    }

    @Override
    @Transactional
    public ClientDTO findById(UUID uuid) {
        log.info("retrieving client by id {}", uuid);
        return clientDTOMapper.mapToClientDTO(
                clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public void update(UUID uuid, ClientDTO updatedClientDTO) {
        Client updatedClient = clientDTOMapper.mapToClient(updatedClientDTO);
        Client client = clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        client = clientUpdateService.update(client, updatedClient);
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
    public List<ClientDTO> findActiveClients() {
        log.info("retrieving list of active clients");
        List<Client> clients = clientRepository.findClientsByStatusIs(ClientStatus.ACTIVE);
        return clientDTOMapper.getListOfAgreementDTOs(clients);
    }

    @Override
    @Transactional
    public List<ClientDTO> findClientsWhereBalanceMoreThan(BigDecimal balance) {
        log.info("retrieving list of clients where balance is more than {}", balance);
        List<Client> clients = clientRepository.findAllClientsWhereBalanceMoreThan(balance);
        return clientDTOMapper.getListOfAgreementDTOs(clients);
    }

    @Override
    @Transactional
    public List<ClientDTO> findClientsWhereTransactionMoreThan(Integer count) {
        log.info("retrieving list of clients where transaction count is more than {}", count);
        List<Client> clients = clientRepository.findAllClientsWhereTransactionMoreThan(count);
        return clientDTOMapper.getListOfAgreementDTOs(clients);
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

    @Override
    @Transactional
    public List<Client> findClientsWithCurrentAndSavingsAccounts() {
        log.info("retrieving clients where status is {} and {}", AccountType.CURRENT, AccountType.SAVINGS);
        List<Client> clients = clientRepository.findAllActiveClientsWithTwoDifferentAccountTypes(
                AccountType.CURRENT, AccountType.SAVINGS);
        log.info(clients.toString());
        return checkListForNull(clients);
    }

    private List<Client> checkListForNull(List<Client> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
