package org.crazymages.bankingspringproject.controller;

import org.crazymages.bankingspringproject.dto.manager.ManagerDto;
import org.crazymages.bankingspringproject.service.database.ManagerDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagerControllerTest {

    @Mock
    ManagerDatabaseService managerDatabaseService;

    @InjectMocks
    ManagerController managerController;

    String uuid;

    @BeforeEach
    void setUp() {
        uuid = "7bcf30be-8c6e-4e10-a73b-706849fc94dc";
    }

    @Test
    void createManager_success() {
        // when
        ManagerDto managerDto = ManagerDto.builder().build();
        ManagerDto createdManagerDto = ManagerDto.builder().build();

        // when
        ResponseEntity<ManagerDto> actual = managerController.createManager(managerDto);

        // then
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(createdManagerDto, actual.getBody());
        verify(managerDatabaseService).create(managerDto);
    }

    @Test
    void createManager_emptyManagerDto_savesNoData() {
        // when
        ResponseEntity<ManagerDto> actual = managerController.createManager(null);

        // then
        assertNull(actual.getBody());
        verify(managerDatabaseService, never()).create(any(ManagerDto.class));
    }

    @Test
    void findAllManagers_success() {
        // given
        List<ManagerDto> expected = List.of(ManagerDto.builder().build(), ManagerDto.builder().build());
        when(managerDatabaseService.findAllNotDeleted()).thenReturn(expected);

        // when
        ResponseEntity<List<ManagerDto>> actual = managerController.findAllManagers();

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(managerDatabaseService).findAllNotDeleted();
    }

    @Test
    void findAllManagers_withEmptyList_returnsNoContentStatus() {
        // given
        List<ManagerDto> expected = Collections.emptyList();
        when(managerDatabaseService.findAllNotDeleted()).thenReturn(expected);

        // when
        ResponseEntity<List<ManagerDto>> actual = managerController.findAllManagers();

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(managerDatabaseService).findAllNotDeleted();
    }

    @Test
    void findManagerByUuid_success() {
        // given
        ManagerDto expected = ManagerDto.builder().build();
        when(managerDatabaseService.findById(uuid)).thenReturn(expected);

        // when
        ResponseEntity<ManagerDto> actual = managerController.findManagerByUuid(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(managerDatabaseService).findById(uuid);
    }

    @Test
    void updateManager_success() {
        // given
        ManagerDto expected = ManagerDto.builder().build();

        // when
        ResponseEntity<ManagerDto> actual = managerController.updateManager(uuid, expected);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(managerDatabaseService).update(uuid, expected);
    }

    @Test
    void deleteManager_success() {
        // when
        ResponseEntity<String> actual = managerController.deleteManager(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        verify(managerDatabaseService).delete(uuid);
    }
}
