package org.crazymages.bankingspringproject.dto.manager.mapper;

import org.crazymages.bankingspringproject.dto.manager.ManagerDto;
import org.crazymages.bankingspringproject.dto.manager.mapper.ManagerDtoMapper;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ManagerDtoMapperTest {

    ManagerDtoMapper managerDtoMapper;
    ManagerDto managerDto;
    Manager manager1;
    Manager manager2;

    @BeforeEach
    void setUp() {
        managerDtoMapper = new ManagerDtoMapper();
        managerDto = ManagerDto.builder().build();

        manager1 = new Manager();
        manager1.setUuid(UUID.randomUUID());
        manager1.setFirstName("John");
        manager1.setLastName("Doe");
        manager1.setStatus(ManagerStatus.ACTIVE);
        manager1.setDescription("Manager 1");

        manager2 = new Manager();
        manager2.setUuid(UUID.randomUUID());
        manager2.setFirstName("Jane");
        manager2.setLastName("Smith");
        manager2.setStatus(ManagerStatus.FIRED);
        manager2.setDescription("Manager 2");
    }

    @Test
    void mapEntityToDto_validManager_success() {
        // when
        ManagerDto managerDto = managerDtoMapper.mapEntityToDto(manager1);

        // then
        assertEquals(manager1.getFirstName(), managerDto.getFirstName());
        assertEquals(manager1.getLastName(), managerDto.getLastName());
        assertEquals(manager1.getStatus().toString(), managerDto.getStatus());
        assertEquals(manager1.getDescription(), managerDto.getDescription());
    }

    @Test
    void mapEntityToDto_missingManagerProperties_returnsManagerDtoWithNullProperties() {
        // given
        Manager manager = new Manager();

        // when
        ManagerDto managerDto = managerDtoMapper.mapEntityToDto(manager);

        // then
        assertNull(managerDto.getFirstName());
        assertNull(managerDto.getLastName());
        assertNull(managerDto.getStatus());
        assertNull(managerDto.getDescription());
    }

    @Test
    void mapEntityToDto_nullManager_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> managerDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validManagerDto_success() {
        // given
        managerDto.setFirstName("John");
        managerDto.setLastName("Doe");
        managerDto.setStatus("ACTIVE");
        managerDto.setDescription("Manager 1");

        // when
        Manager manager = managerDtoMapper.mapDtoToEntity(managerDto);

        // then
        assertFalse(manager.isDeleted());
        assertEquals(managerDto.getFirstName(), manager.getFirstName());
        assertEquals(managerDto.getLastName(), manager.getLastName());
        assertEquals(ManagerStatus.valueOf(managerDto.getStatus()), manager.getStatus());
        assertEquals(managerDto.getDescription(), manager.getDescription());
    }

    @Test
    void mapDtoToEntity_missingManagerDtoProperties_returnsManagerWithNullProperties() {
        // given
        ManagerDto managerDto = ManagerDto.builder().build();

        // when
        Manager manager = managerDtoMapper.mapDtoToEntity(managerDto);

        // then
        assertFalse(manager.isDeleted());
        assertNull(manager.getFirstName());
        assertNull(manager.getLastName());
        assertNull(manager.getStatus());
        assertNull(manager.getDescription());
    }

    @Test
    void mapDtoToEntity_nullManagerDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> managerDtoMapper.mapDtoToEntity(null));
    }
}
