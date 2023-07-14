package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.ManagerDTO;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ManagerRepository;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.ManagerDTOMapper;
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
    private final ManagerDTOMapper managerDTOMapper;


    @Override
    @Transactional
    public void create(ManagerDTO managerDTO) {
        if (managerDTO == null) {
            throw new IllegalArgumentException();
        }
        Manager manager = managerDTOMapper.mapDtoToEntity(managerDTO);
        managerRepository.save(manager);
        log.info("manager created");
    }

    @Override
    @Transactional
    public List<ManagerDTO> findAll() {
        log.info("retrieving list of managers");
        List<Manager> managers = managerRepository.findAll();
        return managerDTOMapper.getListOfDTOs(managers);
    }

    @Override
    @Transactional
    public List<ManagerDTO> findAllNotDeleted() {
        log.info("retrieving list of managers");
        List<Manager> managers = managerRepository.findAllNotDeleted();
        return managerDTOMapper.getListOfDTOs(managers);
    }

    @Override
    @Transactional
    public List<ManagerDTO> findDeletedAccounts() {
        log.info("retrieving list of deleted managers");
        List<Manager> deletedManagers = managerRepository.findAllDeleted();
        return managerDTOMapper.getListOfDTOs(deletedManagers);
    }

    @Override
    @Transactional
    public ManagerDTO findById(UUID uuid) {
        log.info("retrieving manager by id {}", uuid);
        return managerDTOMapper.mapEntityToDto(
                managerRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public void update(UUID uuid, ManagerDTO updatedManagerDTO) {
        Manager managerUpdate = managerDTOMapper.mapDtoToEntity(updatedManagerDTO);
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
        return managers == null ? Collections.emptyList() : managers;
    }

    @Override
    @Transactional
    public List<Manager> findManagersSortedByProductQuantityWhereManagerStatusIs(ManagerStatus status) {
        log.info("retrieving list of managers sorted by status {}", status);
        List<Manager> managers = managerRepository.findAllManagersSortedByProductQuantityWhereManagerStatusIs(status);
        return managers == null ? Collections.emptyList() : managers;
    }

    @Override
    public Manager getFirstManager(List<Manager> managers) {
        return managers
                .stream()
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException("null"));
    }
}
