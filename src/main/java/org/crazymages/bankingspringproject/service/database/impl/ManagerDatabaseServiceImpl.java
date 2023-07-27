package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.ManagerDto;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ManagerRepository;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.crazymages.bankingspringproject.dto.mapper.manager.ManagerDtoMapper;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * A service implementation for managing Manager entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerDatabaseServiceImpl implements ManagerDatabaseService {

    private final ManagerRepository managerRepository;
    private final EntityUpdateService<Manager> managerUpdateService;
    private final ManagerDtoMapper managerDtoMapper;


    @Override
    @Transactional
    public void create(ManagerDto managerDto) {
        if (managerDto == null) {
            throw new IllegalArgumentException();
        }
        Manager manager = managerDtoMapper.mapDtoToEntity(managerDto);
        managerRepository.save(manager);
        log.info("manager created");
    }

    @Override
    @Transactional
    public List<Manager> findAll() {
        log.info("retrieving list of managers");
        return managerRepository.findAll();
    }

    @Override
    @Transactional
    public List<ManagerDto> findAllNotDeleted() {
        log.info("retrieving list of managers");
        List<Manager> managers = managerRepository.findAllNotDeleted();
        return getDtoList(managers);
    }

    @Override
    @Transactional
    public List<ManagerDto> findDeletedAccounts() {
        log.info("retrieving list of deleted managers");
        List<Manager> managers = managerRepository.findAllDeleted();
        return getDtoList(managers);
    }

    @Override
    @Transactional
    public ManagerDto findById(String managerUuid) {
        if (managerUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(managerUuid);
        log.info("retrieving manager by id {}", uuid);
        return managerDtoMapper.mapEntityToDto(
                managerRepository.findById(uuid)
                        .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public void update(String managerUuid, ManagerDto updatedManagerDto) {
        if (managerUuid == null || updatedManagerDto == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(managerUuid);
        Manager managerUpdate = managerDtoMapper.mapDtoToEntity(updatedManagerDto);
        Manager manager = managerRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        manager = managerUpdateService.update(manager, managerUpdate);
        managerRepository.save(manager);
        log.info("updated manager id {}", uuid);
    }

    @Override
    @Transactional
    public void delete(String managerUuid) {
        if (managerUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(managerUuid);
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

    private List<ManagerDto> getDtoList(List<Manager> managers) {
        return Optional.ofNullable(managers)
                .orElse(Collections.emptyList())
                .stream()
                .map(managerDtoMapper::mapEntityToDto)
                .toList();
    }
}
