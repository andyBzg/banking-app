package org.crazymages.bankingspringproject.service.utils.updater.impl;

import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerUpdateServiceImplTest {

    ManagerUpdateServiceImpl managerUpdateService;
    Manager manager;
    Manager managerUpdate;

    @BeforeEach
    void setUp() {
        managerUpdateService = new ManagerUpdateServiceImpl();

        manager = new Manager();
        manager.setFirstName("John");
        manager.setLastName("Doe");
        manager.setStatus(ManagerStatus.ACTIVE);
        manager.setDescription("Manager Description");

        managerUpdate = new Manager();
        managerUpdate.setFirstName("Jane");
        managerUpdate.setLastName("Smith");
        managerUpdate.setStatus(ManagerStatus.UNAVAILABLE);
        managerUpdate.setDescription("Updated Manager Description");
    }

    @Test
    void update_withValidFields_updatesManagerProperties() {
        // when
        Manager actual = managerUpdateService.update(manager, managerUpdate);

        // then
        assertEquals(managerUpdate.getFirstName(), actual.getFirstName());
        assertEquals(managerUpdate.getLastName(), actual.getLastName());
        assertEquals(managerUpdate.getStatus(), actual.getStatus());
        assertEquals(managerUpdate.getDescription(), actual.getDescription());
    }

    @Test
    void update_withNullManager_doesNotUpdateManager_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> managerUpdateService.update(null, managerUpdate));
    }

    @Test
    void update_withNullManagerUpdate_doesNotUpdateManager_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> managerUpdateService.update(manager, null));
    }

    @Test
    void updateProperties_managerUpdateWithNullFields_updatesOnlyExistentProperties() {
        // given
        Manager managerUpdate = new Manager();
        managerUpdate.setFirstName("Jane");
        managerUpdate.setLastName("Smith");
        managerUpdate.setStatus(null);
        managerUpdate.setDescription(null);

        // when
        Manager actual = managerUpdateService.updateProperties(manager, managerUpdate);

        // then
        assertEquals(managerUpdate.getFirstName(), actual.getFirstName());
        assertEquals(managerUpdate.getLastName(), actual.getLastName());
        assertEquals(manager.getStatus(), actual.getStatus());
        assertEquals(manager.getDescription(), actual.getDescription());
    }

    @Test
    void updateProperties_managerWithNullProperties_returnsManagerWithUpdatedProperties() {
        // given
        Manager manager = new Manager();

        // when
        Manager actual = managerUpdateService.updateProperties(manager, managerUpdate);

        // then
        assertEquals(managerUpdate.getFirstName(), actual.getFirstName());
        assertEquals(managerUpdate.getLastName(), actual.getLastName());
        assertEquals(managerUpdate.getStatus(), actual.getStatus());
        assertEquals(managerUpdate.getDescription(), actual.getDescription());
    }

    @Test
    void updateProperties_updateWithNullProperties_doesNotUpdateClientProperties() {
        // given
        Manager managerUpdate1 = new Manager();

        // when
        Manager actual = managerUpdateService.updateProperties(manager, managerUpdate1);

        // then
        assertEquals(manager.getFirstName(), actual.getFirstName());
        assertEquals(manager.getLastName(), actual.getLastName());
        assertEquals(manager.getStatus(), actual.getStatus());
        assertEquals(manager.getDescription(), actual.getDescription());
    }
}
