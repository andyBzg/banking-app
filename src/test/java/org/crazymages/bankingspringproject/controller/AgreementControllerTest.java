package org.crazymages.bankingspringproject.controller;

import org.crazymages.bankingspringproject.dto.agreement.AgreementDto;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgreementControllerTest {

    @Mock
    AgreementDatabaseService agreementDatabaseService;

    @InjectMocks
    AgreementController agreementController;

    String uuid;

    @BeforeEach
    void setUp() {
        uuid = "7bcf30be-8c6e-4e10-a73b-706849fc94dc";
    }

    @Test
    void createAgreement_success() {
        // given
        AgreementDto agreementDto = AgreementDto.builder().build();
        AgreementDto createdAgreementDto = AgreementDto.builder().build();

        // when
        ResponseEntity<AgreementDto> actual = agreementController.createAgreement(agreementDto);

        // then
        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(createdAgreementDto, actual.getBody());
        verify(agreementDatabaseService).create(agreementDto);
    }

    @Test
    void findAllAgreements_success() {
        // given
        List<AgreementDto> expected = List.of(AgreementDto.builder().build(), AgreementDto.builder().build());
        when(agreementDatabaseService.findAllNotDeleted()).thenReturn(expected);

        // when
        ResponseEntity<List<AgreementDto>> actual = agreementController.findAllAgreements();

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(agreementDatabaseService).findAllNotDeleted();
    }

    @Test
    void findAllAgreements_withEmptyList_returnsNoContentStatus() {
        // given
        List<AgreementDto> expected = Collections.emptyList();
        when(agreementDatabaseService.findAllNotDeleted()).thenReturn(expected);

        // when
        ResponseEntity<List<AgreementDto>> actual = agreementController.findAllAgreements();

        // then
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertNull(actual.getBody());
        verify(agreementDatabaseService).findAllNotDeleted();
    }

    @Test
    void findAgreementByUuid_success() {
        // given
        AgreementDto expected = AgreementDto.builder().build();
        when(agreementDatabaseService.findById(uuid)).thenReturn(expected);

        // when
        ResponseEntity<AgreementDto> actual = agreementController.findAgreementByUuid(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(agreementDatabaseService).findById(uuid);
    }

    @Test
    void updateAgreement_success() {
        // given
        AgreementDto expected = AgreementDto.builder().build();

        // when
        ResponseEntity<AgreementDto> actual = agreementController.updateAgreement(uuid, expected);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(agreementDatabaseService).update(uuid, expected);
    }

    @Test
    void deleteAgreement_success() {
        // when
        ResponseEntity<String> actual = agreementController.deleteAccount(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        verify(agreementDatabaseService).delete(uuid);
    }

    @Test
    void findAgreementsByManagerId_success() {
        // given
        List<AgreementDto> expected = List.of(AgreementDto.builder().build(), AgreementDto.builder().build());
        when(agreementDatabaseService.findAgreementsByManagerUuid(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<AgreementDto>> actual = agreementController.findAgreementsByManagerId(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(agreementDatabaseService).findAgreementsByManagerUuid(uuid);
    }

    @Test
    void findAgreementsByClientId_success() {
        // given
        List<AgreementDto> expected = List.of(AgreementDto.builder().build(), AgreementDto.builder().build());
        when(agreementDatabaseService.findAgreementDtoListByClientUuid(uuid)).thenReturn(expected);

        // when
        ResponseEntity<List<AgreementDto>> actual = agreementController.findAgreementsByClientId(uuid);

        // then
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(expected, actual.getBody());
        verify(agreementDatabaseService).findAgreementDtoListByClientUuid(uuid);
    }
}
