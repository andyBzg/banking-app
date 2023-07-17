package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.ManagerDto;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
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
        managerDto = new ManagerDto();

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
        assertEquals(manager1.getUuid().toString(), managerDto.getUuid());
        assertEquals(manager1.getFirstName(), managerDto.getFirstName());
        assertEquals(manager1.getLastName(), managerDto.getLastName());
        assertEquals(manager1.getStatus().toString(), managerDto.getStatus());
        assertEquals(manager1.getDescription(), managerDto.getDescription());
    }

    @Test
    void mapEntityToDto_nullManager_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> managerDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validManagerDto_success() {
        // given
        managerDto.setUuid("30348dce-45f7-4e19-aa08-3ed77a8f7ac3");
        managerDto.setFirstName("John");
        managerDto.setLastName("Doe");
        managerDto.setStatus("ACTIVE");
        managerDto.setDescription("Manager 1");

        // when
        Manager manager = managerDtoMapper.mapDtoToEntity(managerDto);

        // then
        assertEquals(UUID.fromString(managerDto.getUuid()), manager.getUuid());
        assertEquals(managerDto.getFirstName(), manager.getFirstName());
        assertEquals(managerDto.getLastName(), manager.getLastName());
        assertEquals(ManagerStatus.valueOf(managerDto.getStatus()), manager.getStatus());
        assertEquals(managerDto.getDescription(), manager.getDescription());
    }

    @Test
    void mapDtoToEntity_nullManagerDto_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> managerDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingManagerDtoProperties_throwsIllegalArgumentException() {
        // given
        managerDto.setUuid("30348dce-45f7-4e19-aa08-3ed77a8f7ac3");

        // when, then
        assertThrows(NullPointerException.class, () -> managerDtoMapper.mapDtoToEntity(managerDto));
    }

    @Test
    void getDtoList_validManagerList_success() {
        // given
        List<Manager> managerList = List.of(manager1, manager2);

        // when
        List<ManagerDto> actual = managerDtoMapper.getDtoList(managerList);

        // then
        assertEquals(2, actual.size());

        ManagerDto managerDto1 = actual.get(0);
        assertEquals(manager1.getUuid().toString(), managerDto1.getUuid());
        assertEquals(manager1.getFirstName(), managerDto1.getFirstName());
        assertEquals(manager1.getLastName(), managerDto1.getLastName());
        assertEquals(manager1.getStatus().toString(), managerDto1.getStatus());
        assertEquals(manager1.getDescription(), managerDto1.getDescription());

        ManagerDto managerDto2 = actual.get(1);
        assertEquals(manager2.getUuid().toString(), managerDto2.getUuid());
        assertEquals(manager2.getFirstName(), managerDto2.getFirstName());
        assertEquals(manager2.getLastName(), managerDto2.getLastName());
        assertEquals(manager2.getStatus().toString(), managerDto2.getStatus());
        assertEquals(manager2.getDescription(), managerDto2.getDescription());
    }

    @Test
    void getDtoList_nullManagerList_throwsDataNotFoundException() {
        assertThrows(DataNotFoundException.class, () -> managerDtoMapper.getDtoList(null));
    }

    @Test
    void getDtoList_emptyManagerList_returnsEmptyList() {
        // given
        List<Manager> managerList = Collections.emptyList();

        // when
        List<ManagerDto> actual = managerDtoMapper.getDtoList(managerList);

        // then
        assertTrue(actual.isEmpty());
    }
}