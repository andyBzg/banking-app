package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.repository.ManagerRepository;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerDatabaseServiceImpl implements ManagerDatabaseService {

    private final ManagerRepository managerRepository;

    @Override
    public void create(Manager manager) {
        managerRepository.save(manager);
    }

    @Override
    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    @Override
    public Manager findById(UUID uuid) {
        Optional<Manager> managerOptional = managerRepository.findById(uuid);
        return managerOptional.orElse(null);
    }

    @Override
    @Transactional
    public Manager update(UUID uuid, Manager managerUpdate) {
        Optional<Manager> managerOptional = managerRepository.findById(uuid);
        if (managerOptional.isPresent()) {
            Manager manager = managerOptional.get();
            manager.setFirstName(managerUpdate.getFirstName());
            manager.setFirstName(managerUpdate.getFirstName());
            manager.setStatus(managerUpdate.getStatus());
            manager.setDescription(managerUpdate.getDescription());
            managerRepository.save(manager);
        }
        return managerOptional.orElse(null);
    }

    @Override
    public void delete(UUID uuid) {
        managerRepository.deleteById(uuid);
    }
}
