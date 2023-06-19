package org.crazymages.bankingspringproject.service.database.updater.impl;

import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.service.database.updater.EntityUpdateService;
import org.springframework.stereotype.Service;

@Service
public class ManagerUpdateServiceImpl implements EntityUpdateService<Manager> {
    @Override
    public Manager update(Manager manager, Manager managerUpdate) {
        if (managerUpdate.getFirstName() != null) {
            manager.setFirstName(managerUpdate.getFirstName());
        }
        if (managerUpdate.getFirstName() != null) {
            manager.setFirstName(managerUpdate.getFirstName());
        }
        if (managerUpdate.getStatus() != null) {
            manager.setStatus(managerUpdate.getStatus());
        }
        if (managerUpdate.getDescription() != null) {
            manager.setDescription(managerUpdate.getDescription());
        }
        return manager;
    }
}
