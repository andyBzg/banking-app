package org.crazymages.bankingspringproject.service.utils.updater.impl;

import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Component;

/**
 * A class implementing the EntityUpdateService interface for updating Manager entities.
 * It provides custom update logic for the Manager entity.
 */
@Component
public class ManagerUpdateServiceImpl implements EntityUpdateService<Manager> {

    /**
     * Updates the given Manager entity with the provided Manager update.
     * Only non-null fields in the Manager update will be applied to the original Manager.
     *
     * @param manager The existing entity to be updated.
     * @param managerUpdate The entity containing the updated data.
     * @return The updated Manager entity.
     */
    @Override
    public Manager update(Manager manager, Manager managerUpdate) {
        if (manager != null && managerUpdate != null) {
            if (managerUpdate.getFirstName() != null) {
                manager.setFirstName(managerUpdate.getFirstName());
            }
            if (managerUpdate.getLastName() != null) {
                manager.setLastName(managerUpdate.getLastName());
            }
            if (managerUpdate.getStatus() != null) {
                manager.setStatus(managerUpdate.getStatus());
            }
            if (managerUpdate.getDescription() != null) {
                manager.setDescription(managerUpdate.getDescription());
            }
        }
        return manager;
    }
}
