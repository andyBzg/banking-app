package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.ManagerDto;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ManagerRepository;
import org.crazymages.bankingspringproject.dto.mapper.manager.ManagerDtoMapper;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerDatabaseServiceImplTest {

    @Mock
    ManagerRepository managerRepository;
    @Mock
    EntityUpdateService<Manager> managerUpdateService;
    @Mock
    ManagerDtoMapper managerDTOMapper;

    @InjectMocks
    ManagerDatabaseServiceImpl managerDatabaseService;

    Manager manager1;
    Manager manager2;
    ManagerDto managerDto1;
    ManagerDto managerDto2;
    UUID uuid;
    List<Manager> managers;

    @BeforeEach
    void setUp() {
        manager1 = new Manager();
        manager2 = new Manager();
        managerDto1 = ManagerDto.builder().build();
        managerDto2 = ManagerDto.builder().build();
        uuid = UUID.randomUUID();
        managers = List.of(manager1, manager2);
    }

    @Test
    void create_success() {
        // given
        when(managerDTOMapper.mapDtoToEntity(managerDto1)).thenReturn(manager1);

        // when
        managerDatabaseService.create(managerDto1);

        // then
        verify(managerDTOMapper).mapDtoToEntity(managerDto1);
        verify(managerRepository).save(manager1);
    }

    @Test
    void create_withNullManagerDTO_throwsIllegalArgumentException() {
        // when
        assertThrows(IllegalArgumentException.class, () -> managerDatabaseService.create(null));

        // then
        verifyNoInteractions(managerRepository);
    }

    @Test
    void findAll_success() {
        // given
        List<Manager> expected = List.of(manager1, manager2);
        when(managerRepository.findAll()).thenReturn(managers);

        // when
        List<Manager> actual = managerDatabaseService.findAll();

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findAll();
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        List<ManagerDto> expected = List.of(managerDto1, managerDto2);
        when(managerRepository.findAllNotDeleted()).thenReturn(managers);
        when(managerDTOMapper.mapEntityToDto(manager1)).thenReturn(managerDto1);
        when(managerDTOMapper.mapEntityToDto(manager2)).thenReturn(managerDto2);

        // when
        List<ManagerDto> actual = managerDatabaseService.findAllNotDeleted();

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findAllNotDeleted();
        verify(managerDTOMapper, times(2)).mapEntityToDto(any(Manager.class));
    }

    @Test
    void findDeletedAccounts_success() {
        // given
        List<ManagerDto> expected = List.of(managerDto1, managerDto2);
        when(managerRepository.findAllDeleted()).thenReturn(managers);
        when(managerDTOMapper.mapEntityToDto(manager1)).thenReturn(managerDto1);
        when(managerDTOMapper.mapEntityToDto(manager2)).thenReturn(managerDto2);

        // when
        List<ManagerDto> actual = managerDatabaseService.findDeletedAccounts();

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findAllDeleted();
        verify(managerDTOMapper, times(2)).mapEntityToDto(any(Manager.class));
    }

    @Test
    void findById_success() {
        // given
        ManagerDto expected = managerDto1;
        when(managerRepository.findById(uuid)).thenReturn(Optional.ofNullable(manager1));
        when(managerDTOMapper.mapEntityToDto(manager1)).thenReturn(managerDto1);

        // when
        ManagerDto actual = managerDatabaseService.findById(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findById(uuid);
        verify(managerDTOMapper).mapEntityToDto(manager1);
    }

    @Test
    void findById_nonExistentManager_throwsDataNotFoundException() {
        // given
        String managerUuid = String.valueOf(uuid);
        when(managerRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> managerDatabaseService.findById(managerUuid));

        // then
        verify(managerRepository).findById(uuid);
    }

    @Test
    void update_success() {
        // given
        ManagerDto updatedManagerDto = managerDto1;
        Manager updatedManager = manager1;
        Manager managerToUpdate = manager2;

        when(managerDTOMapper.mapDtoToEntity(updatedManagerDto)).thenReturn(updatedManager);
        when(managerRepository.findById(uuid)).thenReturn(Optional.ofNullable(managerToUpdate));
        when(managerUpdateService.update(managerToUpdate, updatedManager)).thenReturn(manager1);


        // when
        managerDatabaseService.update(String.valueOf(uuid), updatedManagerDto);


        // then
        verify(managerDTOMapper).mapDtoToEntity(updatedManagerDto);
        verify(managerRepository).findById(uuid);
        verify(managerUpdateService).update(managerToUpdate, updatedManager);
        verify(managerRepository).save(manager1);
    }

    @Test
    void update_nonExistentManager_throwsDataNotFoundException() {
        // given
        String managerUuid = String.valueOf(uuid);
        ManagerDto updatedManagerDto = managerDto1;

        when(managerRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> managerDatabaseService.update(managerUuid, updatedManagerDto));

        // then
        verify(managerRepository).findById(uuid);
        verifyNoInteractions(managerUpdateService);
        verify(managerUpdateService, times(0)).update(any(Manager.class) , any(Manager.class));
        verify(managerRepository, times(0)).save(any(Manager.class));
    }

    @Test
    void delete_success() {
        // given
        when(managerRepository.findById(uuid)).thenReturn(Optional.ofNullable(manager1));

        // when
        managerDatabaseService.delete(String.valueOf(uuid));

        // then
        verify(managerRepository).findById(uuid);
        verify(managerRepository).save(manager1);
        assertTrue(manager1.isDeleted());
    }

    @Test
    void delete_nonExistentManager_throwsDataNotFoundException() {
        // given
        String managerUuid = String.valueOf(uuid);
        when(managerRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> managerDatabaseService.delete(managerUuid));

        // then
        verify(managerRepository).findById(uuid);
        verify(managerRepository, times(0)).save(any(Manager.class));
    }

    @Test
    void findManagersSortedByClientQuantityWhereManagerStatusIs_success() {
        // given
        List<Manager> expected = List.of(manager1, manager2);
        ManagerStatus status = ManagerStatus.TRANSFERRED;
        when(managerRepository.findManagersSortedByClientCountWhereManagerStatusIs(status)).thenReturn(managers);

        // when
        List<Manager> actual = managerDatabaseService.findManagersSortedByClientQuantityWhereManagerStatusIs(status);

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findManagersSortedByClientCountWhereManagerStatusIs(status);
    }

    @Test
    void findManagersSortedByProductQuantityWhereManagerStatusIs_success() {
        // given
        List<Manager> expected = List.of(manager1, manager2);
        ManagerStatus status = ManagerStatus.ACTIVE;
        when(managerRepository.findAllManagersSortedByProductQuantityWhereManagerStatusIs(status)).thenReturn(managers);

        // when
        List<Manager> actual = managerDatabaseService.findManagersSortedByProductQuantityWhereManagerStatusIs(status);

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findAllManagersSortedByProductQuantityWhereManagerStatusIs(status);
    }

    @Test
    void getFirstManager_success() {
        // given
        List<Manager> managers = List.of(manager1, manager2);
        Manager expected = manager1;

        // when
        Manager actual = managerDatabaseService.getFirstManager(managers);

        // then
        assertEquals(expected, actual);
    }
}
