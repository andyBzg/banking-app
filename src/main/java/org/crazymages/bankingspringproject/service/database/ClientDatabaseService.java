package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ClientDatabaseService {

    void create(Client client);

    List<Client> findAll();

    List<Client> findAllNotDeleted();

    List<Client> findDeletedAccounts();

    Client findById(UUID uuid);

    void update(UUID uuid, Client client);

    void delete(UUID uuid);

    List<Client> findActiveClients();

    List<Client> findClientsWhereBalanceMoreThan(BigDecimal balance);

    List<Client> findClientsWhereTransactionMoreThan(Integer count);

    BigDecimal calculateTotalBalanceByClientUuid(UUID uuid);

    boolean isClientStatusActive(UUID uuid);

    List<Client> findClientsWithCurrentAndSavingsAccounts();
}
