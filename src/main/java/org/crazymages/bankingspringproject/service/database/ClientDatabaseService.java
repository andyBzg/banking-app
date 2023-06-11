package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Client;

import java.util.List;
import java.util.UUID;

public interface ClientDatabaseService {

    void create(Client client);

    List<Client> findAll();

    Client findById(UUID uuid);

    Client update(UUID uuid, Client client);

    void delete(UUID uuid);
}
