package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ManagerRepository;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * A service implementation for managing Manager entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerDatabaseServiceImpl implements ManagerDatabaseService {

    private final ManagerRepository managerRepository;
    private final EntityUpdateService<Manager> managerUpdateService;


    @Override
    @Transactional
    public void create(Manager manager) {
        managerRepository.save(manager);
        log.info("manager created");
    }

    @Override
    @Transactional
    public List<Manager> findAll() {
        log.info("retrieving list of managers");
        List<Manager> managers = managerRepository.findAll();
        return checkListForNull(managers);
    }

    @Override
    @Transactional
    public List<Manager> findAllNotDeleted() {
        log.info("retrieving list of managers");
        List<Manager> managers = managerRepository.findAllNotDeleted();
        return checkListForNull(managers);
    }

    @Override
    @Transactional
    public List<Manager> findDeletedAccounts() {
        log.info("retrieving list of deleted managers");
        List<Manager> deletedManagers = managerRepository.findAllDeleted();
        return checkListForNull(deletedManagers);
    }

    @Override
    @Transactional
    public Manager findById(UUID uuid) {
        log.info("retrieving manager by id {}", uuid);
        return managerRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public void update(UUID uuid, Manager managerUpdate) {
        Manager manager = managerRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        manager = managerUpdateService.update(manager, managerUpdate);
        managerRepository.save(manager);
        log.info("updated manager id {}", uuid);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        Manager manager = managerRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        manager.setDeleted(true);
        managerRepository.save(manager);
        log.info("deleted manager id {}", uuid);
    }

    @Override
    @Transactional
    public List<Manager> findManagersSortedByClientQuantityWhereManagerStatusIs(ManagerStatus status) {
        log.info("retrieving list of managers sorted by status {}", status);
        List<Manager> managers = managerRepository.findManagersSortedByClientCountWhereManagerStatusIs(status);
        return checkListForNull(managers);
    }

    @Override
    @Transactional
    public List<Manager> findManagersSortedByProductQuantityWhereManagerStatusIs(ManagerStatus status) {
        log.info("retrieving list of managers sorted by status {}", status);
        List<Manager> managers = managerRepository.findAllManagersSortedByProductQuantityWhereManagerStatusIs(status);
        return checkListForNull(managers);
    }

    @Override
    public Manager getFirstManager(List<Manager> activeManagers) {
        return activeManagers
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("null"));
    }

    private List<Manager> checkListForNull(List<Manager> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
