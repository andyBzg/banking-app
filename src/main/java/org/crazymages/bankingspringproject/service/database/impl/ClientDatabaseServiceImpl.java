package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ClientRepository;
import org.crazymages.bankingspringproject.service.database.ClientDatabaseService;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.crazymages.bankingspringproject.service.utils.validator.ListValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final ListValidator<Client> listValidator;

    /**
     * Creates a new Client entity and saves it to the database. If the manager UUID is not
     * specified, the first active manager is assigned as the client's manager.
     *
     * @param client The Client entity to create.
     */
    @Override
    @Transactional
    public void create(Client client) {
        if (client.getManagerUuid() == null) {
            List<Manager> activeManagers = managerDatabaseService
                    .findManagersSortedByClientQuantityWhereManagerStatusIs(ManagerStatus.ACTIVE);
            Manager firstManager = managerDatabaseService.getFirstManager(activeManagers);
            client.setManagerUuid(firstManager.getUuid());
        }
        clientRepository.save(client);
        log.info("client created");
    }

    /**
     * Retrieves a list of all Client entities from the database.
     *
     * @return A list of all Client entities.
     */
    @Override
    public List<Client> findAll() {
        log.info("retrieving list of clients");
        List<Client> clients = clientRepository.findAll();
        return listValidator.validate(clients);
    }

    /**
     * Retrieves a list of all not deleted Client entities from the database.
     *
     * @return A list of all not deleted Client entities.
     */
    @Override
    @Transactional
    public List<Client> findAllNotDeleted() {
        log.info("retrieving list of clients");
        List<Client> clients = clientRepository.findAllNotDeleted();
        return listValidator.validate(clients);
    }

    /**
     * Retrieves a list of all deleted Client entities from the database.
     *
     * @return A list of all deleted Client entities.
     */
    @Override
    @Transactional
    public List<Client> findDeletedAccounts() {
        log.info("retrieving list of deleted agreements");
        List<Client> deletedClients = clientRepository.findAllDeleted();
        return listValidator.validate(deletedClients);
    }

    /**
     * Retrieves a Client entity from the database by its UUID.
     *
     * @param uuid The UUID of the Client entity to retrieve.
     * @return The Client entity with the specified UUID.
     * @throws DataNotFoundException if no Client entity is found with the specified UUID.
     */
    @Override
    public Client findById(UUID uuid) {
        log.info("retrieving client by id {}", uuid);
        return clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    /**
     * Updates an existing Client entity in the database.
     *
     * @param uuid         The UUID of the Client to update.
     * @param clientUpdate The updated Client entity.
     * @throws DataNotFoundException if no Client entity is found with the specified UUID.
     */
    @Override
    @Transactional
    public void update(UUID uuid, Client clientUpdate) {
        Client client = clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        client = clientUpdateService.update(client, clientUpdate);
        clientRepository.save(client);
        log.info("updated client id {}", uuid);
    }

    /**
     * Deletes an existing Client entity in the database.
     *
     * @param uuid The UUID of the Client to delete.
     * @throws DataNotFoundException if no Client entity is found with the specified UUID.
     */
    @Override
    @Transactional
    public void delete(UUID uuid) {
        Client client = clientRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        client.setDeleted(true);
        clientRepository.save(client);
        log.info("deleted client id {}", uuid);
    }

    /**
     * Retrieves a list of active Client entities from the database.
     *
     * @return A list of active Client entities.
     */
    @Override
    @Transactional
    public List<Client> findActiveClients() {
        log.info("retrieving list of active clients");
        List<Client> clients = clientRepository.findClientsByStatusIs(ClientStatus.ACTIVE);
        return listValidator.validate(clients);
    }

    /**
     * Retrieves a list of Client entities where the balance is greater than the specified amount.
     *
     * @param balance The minimum balance to filter by.
     * @return A list of Client entities where the balance is greater than the specified amount.
     */
    @Override
    @Transactional
    public List<Client> findClientsWhereBalanceMoreThan(BigDecimal balance) {
        log.info("retrieving list of clients where balance is more than {}", balance);
        List<Client> clients = clientRepository.findAllClientsWhereBalanceMoreThan(balance);
        return listValidator.validate(clients);
    }

    /**
     * Retrieves a list of Client entities where the transaction count is greater than the specified value.
     *
     * @param count The minimum transaction count to filter by.
     * @return A list of Client entities where the transaction count is greater than the specified value.
     */
    @Override
    @Transactional
    public List<Client> findClientsWhereTransactionMoreThan(Integer count) {
        log.info("retrieving list of clients where transaction count is more than {}", count);
        List<Client> clients = clientRepository.findAllClientsWhereTransactionMoreThan(count);
        return listValidator.validate(clients);
    }

    /**
     * Calculates the total balance of a Client entity by its UUID.
     *
     * @param uuid The UUID of the Client entity.
     * @return The total balance of the Client entity.
     */
    @Override
    @Transactional
    public BigDecimal calculateTotalBalanceByClientUuid(UUID uuid) {
        log.info("calculating total balance for client id {}", uuid);
        return clientRepository.calculateTotalBalanceByClientUuid(uuid);
    }

    /**
     * Checks if the status of a Client entity is active.
     *
     * @param uuid The UUID of the Client entity.
     * @return true if the Client's status is active, false otherwise.
     */
    @Override
    @Transactional
    public boolean isClientStatusActive(UUID uuid) {
        log.info("checking status for client id {}", uuid);
        return clientRepository.isClientStatusBlocked(uuid);
    }

    /**
     * Retrieves a list of Client entities that have both current and savings accounts.
     *
     * @return A list of Client entities with both current and savings accounts.
     */
    @Override
    @Transactional
    public List<Client> findClientsWithCurrentAndSavingsAccounts() {
        log.info("retrieving clients where status is {} and {}", AccountType.CURRENT, AccountType.SAVINGS);
        List<Client> clients = clientRepository.findAllActiveClientsWithTwoDifferentAccountTypes(
                AccountType.CURRENT, AccountType.SAVINGS);
        log.info(clients.toString());
        return listValidator.validate(clients);
    }
}
