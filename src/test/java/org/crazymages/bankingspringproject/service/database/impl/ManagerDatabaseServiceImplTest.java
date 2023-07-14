package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.ManagerDTO;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.ManagerRepository;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.ManagerDTOMapper;
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
    ManagerDTOMapper managerDTOMapper;

    @InjectMocks
    ManagerDatabaseServiceImpl managerDatabaseService;

    Manager manager1;
    Manager manager2;
    ManagerDTO managerDTO1;
    ManagerDTO managerDTO2;
    UUID uuid;
    List<Manager> managers;

    @BeforeEach
    void setUp() {
        manager1 = new Manager();
        manager2 = new Manager();
        managerDTO1 = new ManagerDTO();
        managerDTO2 = new ManagerDTO();
        uuid = UUID.randomUUID();
        managers = List.of(manager1, manager2);
    }

    @Test
    void create_success() {
        // given
        when(managerDTOMapper.mapDtoToEntity(managerDTO1)).thenReturn(manager1);

        // when
        managerDatabaseService.create(managerDTO1);

        // then
        verify(managerDTOMapper).mapDtoToEntity(managerDTO1);
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
        List<ManagerDTO> expected = List.of(managerDTO1, managerDTO2);
        when(managerRepository.findAll()).thenReturn(managers);
        when(managerDTOMapper.getListOfDTOs(managers)).thenReturn(expected);

        // when
        List<ManagerDTO> actual = managerDatabaseService.findAll();

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findAll();
        verify(managerDTOMapper).getListOfDTOs(managers);
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        List<ManagerDTO> expected = List.of(managerDTO1, managerDTO2);
        when(managerRepository.findAllNotDeleted()).thenReturn(managers);
        when(managerDTOMapper.getListOfDTOs(managers)).thenReturn(expected);

        // when
        List<ManagerDTO> actual = managerDatabaseService.findAllNotDeleted();

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findAllNotDeleted();
        verify(managerDTOMapper).getListOfDTOs(managers);
    }

    @Test
    void findDeletedAccounts_success() {
        // given
        List<ManagerDTO> expected = List.of(managerDTO1, managerDTO2);
        when(managerRepository.findAllDeleted()).thenReturn(managers);
        when(managerDTOMapper.getListOfDTOs(managers)).thenReturn(expected);

        // when
        List<ManagerDTO> actual = managerDatabaseService.findDeletedAccounts();

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findAllDeleted();
        verify(managerDTOMapper).getListOfDTOs(managers);
    }

    @Test
    void findById_success() {
        // given
        ManagerDTO expected = managerDTO1;
        when(managerRepository.findById(uuid)).thenReturn(Optional.ofNullable(manager1));
        when(managerDTOMapper.mapEntityToDto(manager1)).thenReturn(managerDTO1);

        // when
        ManagerDTO actual = managerDatabaseService.findById(uuid);

        // then
        assertEquals(expected, actual);
        verify(managerRepository).findById(uuid);
        verify(managerDTOMapper).mapEntityToDto(manager1);
    }

    @Test
    void findById_nonExistentManager_throwsDataNotFoundException() {
        // given
        when(managerRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> managerDatabaseService.findById(uuid));

        // then
        verify(managerRepository).findById(uuid);
    }

    @Test
    void update_success() {
        // given
        ManagerDTO updatedManagerDTO = managerDTO1;
        Manager updatedManager = manager1;
        Manager managerToUpdate = manager2;

        when(managerDTOMapper.mapDtoToEntity(updatedManagerDTO)).thenReturn(updatedManager);
        when(managerRepository.findById(uuid)).thenReturn(Optional.ofNullable(managerToUpdate));
        when(managerUpdateService.update(managerToUpdate, updatedManager)).thenReturn(manager1);


        // when
        managerDatabaseService.update(uuid, updatedManagerDTO);


        // then
        verify(managerDTOMapper).mapDtoToEntity(updatedManagerDTO);
        verify(managerRepository).findById(uuid);
        verify(managerUpdateService).update(managerToUpdate, updatedManager);
        verify(managerRepository).save(manager1);
    }

    @Test
    void update_nonExistentManager_throwsDataNotFoundException() {
        // given
        ManagerDTO updatedManagerDTO = managerDTO1;

        when(managerRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> managerDatabaseService.update(uuid, updatedManagerDTO));

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
        managerDatabaseService.delete(uuid);

        // then
        verify(managerRepository).findById(uuid);
        verify(managerRepository).save(manager1);
        assertTrue(manager1.isDeleted());
    }

    @Test
    void delete_nonExistentManager_throwsDataNotFoundException() {
        // given
        when(managerRepository.findById(uuid)).thenReturn(Optional.empty());

        // when
        assertThrows(DataNotFoundException.class, () -> managerDatabaseService.delete(uuid));

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
