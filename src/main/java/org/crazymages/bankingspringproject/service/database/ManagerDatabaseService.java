package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;

import java.util.List;
import java.util.UUID;

public interface ManagerDatabaseService {

    void create(Manager manager);

    List<Manager> findAll();

    Manager findById(UUID uuid);

    void update(UUID uuid, Manager manager);

    void delete(UUID uuid);

    List<Manager> findManagersSortedByClientCount(ManagerStatus status);
}
