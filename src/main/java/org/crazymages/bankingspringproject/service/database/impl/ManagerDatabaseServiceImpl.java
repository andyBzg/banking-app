package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ManagerRepository;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.crazymages.bankingspringproject.service.utils.validator.ListValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerDatabaseServiceImpl implements ManagerDatabaseService {

    private final ManagerRepository managerRepository;
    private final EntityUpdateService<Manager> managerUpdateService;
    private final ListValidator<Manager> listValidator;

    @Override
    public void create(Manager manager) {
        managerRepository.save(manager);
        log.info("manager created");
    }

    @Override
    public List<Manager> findAll() {
        log.info("retrieving list of managers");
        List<Manager> managers = managerRepository.findAll();
        return listValidator.validate(managers);
    }

    @Override
    @Transactional
    public List<Manager> findAllNotDeleted() {
        log.info("retrieving list of managers");
        List<Manager> managers = managerRepository.findAllNotDeleted();
        return listValidator.validate(managers);
    }

    @Override
    @Transactional
    public List<Manager> findDeletedAccounts() {
        log.info("retrieving list of deleted managers");
        List<Manager> deletedManagers = managerRepository.findAllDeleted();
        return listValidator.validate(deletedManagers);
    }

    @Override
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
    public List<Manager> findManagersSortedByClientQuantity(ManagerStatus status) {
        log.info("retrieving list of managers sorted by status {}", status);
        List<Manager> managers = managerRepository.findManagersSortedByClientCountWhereManagerStatusIs(status);
        return listValidator.validate(managers);
    }

    @Override
    @Transactional
    public List<Manager> findManagersSortedByProductQuantity(ManagerStatus status) {
        log.info("retrieving list of managers sorted by status {}", status);
        List<Manager> managers = managerRepository.findAllManagersSortedByProductQuantityWhereManagerStatusIs(status);
        return listValidator.validate(managers);
    }


    @Override
    public Manager getFirstManager(List<Manager> activeManagers) {
        return activeManagers
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("null"));
    }
}
