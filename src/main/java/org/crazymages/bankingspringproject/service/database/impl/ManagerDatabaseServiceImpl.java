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

/**
 * A service implementation for managing Manager entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerDatabaseServiceImpl implements ManagerDatabaseService {

    private final ManagerRepository managerRepository;
    private final EntityUpdateService<Manager> managerUpdateService;
    private final ListValidator<Manager> listValidator;

    /**
     * Creates a new Manager entity and saves it to the database.
     *
     * @param manager The Manager entity to create.
     */
    @Override
    public void create(Manager manager) {
        managerRepository.save(manager);
        log.info("manager created");
    }

    /**
     * Retrieves a list of all Manager entities from the database.
     *
     * @return A list of all Manager entities.
     */
    @Override
    public List<Manager> findAll() {
        log.info("retrieving list of managers");
        List<Manager> managers = managerRepository.findAll();
        return listValidator.validate(managers);
    }

    /**
     * Retrieves a list of all not deleted Manager entities from the database.
     *
     * @return A list of all not deleted Manager entities.
     */
    @Override
    @Transactional
    public List<Manager> findAllNotDeleted() {
        log.info("retrieving list of managers");
        List<Manager> managers = managerRepository.findAllNotDeleted();
        return listValidator.validate(managers);
    }

    /**
     * Retrieves a list of all deleted Manager entities from the database.
     *
     * @return A list of all deleted Manager entities.
     */
    @Override
    @Transactional
    public List<Manager> findDeletedAccounts() {
        log.info("retrieving list of deleted managers");
        List<Manager> deletedManagers = managerRepository.findAllDeleted();
        return listValidator.validate(deletedManagers);
    }

    /**
     * Retrieves a Manager entity from the database by its UUID.
     *
     * @param uuid The UUID of the Manager entity to retrieve.
     * @return The Manager entity with the specified UUID.
     * @throws DataNotFoundException if no Manager entity is found with the specified UUID.
     */
    @Override
    public Manager findById(UUID uuid) {
        log.info("retrieving manager by id {}", uuid);
        return managerRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    /**
     * Updates an existing Manager entity in the database.
     *
     * @param uuid          The UUID of the Manager to update.
     * @param managerUpdate The updated Manager entity.
     * @throws DataNotFoundException if no Manager entity is found with the specified UUID.
     */
    @Override
    @Transactional
    public void update(UUID uuid, Manager managerUpdate) {
        Manager manager = managerRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        manager = managerUpdateService.update(manager, managerUpdate);
        managerRepository.save(manager);
        log.info("updated manager id {}", uuid);
    }

    /**
     * Deletes an existing Manager entity in the database.
     *
     * @param uuid The UUID of the Manager to delete.
     * @throws DataNotFoundException if no Manager entity is found with the specified UUID.
     */
    @Override
    @Transactional
    public void delete(UUID uuid) {
        Manager manager = managerRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        manager.setDeleted(true);
        managerRepository.save(manager);
        log.info("deleted manager id {}", uuid);
    }

    /**
     * Retrieves a list of managers sorted by the quantity of clients they manage,
     * where the manager's status matches the specified status.
     *
     * @param status The status of the managers to filter by.
     * @return A list of managers sorted by client quantity.
     */
    @Override
    @Transactional
    public List<Manager> findManagersSortedByClientQuantityWhereManagerStatusIs(ManagerStatus status) {
        log.info("retrieving list of managers sorted by status {}", status);
        List<Manager> managers = managerRepository.findManagersSortedByClientCountWhereManagerStatusIs(status);
        return listValidator.validate(managers);
    }

    /**
     * Retrieves a list of managers sorted by the quantity of products they manage,
     * where the manager's status matches the specified status.
     *
     * @param status The status of the managers to filter by.
     * @return A list of managers sorted by product quantity.
     */
    @Override
    @Transactional
    public List<Manager> findManagersSortedByProductQuantityWhereManagerStatusIs(ManagerStatus status) {
        log.info("retrieving list of managers sorted by status {}", status);
        List<Manager> managers = managerRepository.findAllManagersSortedByProductQuantityWhereManagerStatusIs(status);
        return listValidator.validate(managers);
    }

    /**
     * Gets the first Manager from the list of active managers.
     *
     * @param activeManagers The list of active managers.
     * @return The first Manager in the list.
     * @throws DataNotFoundException if the list of active managers is empty.
     */
    @Override
    public Manager getFirstManager(List<Manager> activeManagers) {
        return activeManagers
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("null"));
    }
}
