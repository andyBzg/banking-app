package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.AgreementDto;
import org.crazymages.bankingspringproject.dto.mapper.agreement.AgreementWithProductDtoMapper;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AgreementRepository;
import org.crazymages.bankingspringproject.dto.mapper.agreement.AgreementDtoMapper;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgreementDatabaseServiceImplTest {

    @Mock
    AgreementRepository agreementRepository;
    @Mock
    EntityUpdateService<Agreement> agreementUpdateService;
    @Mock
    AgreementDtoMapper agreementDTOMapper;
    @Mock
    AgreementWithProductDtoMapper agreementWithProductDtoMapper;

    @InjectMocks
    AgreementDatabaseServiceImpl agreementDatabaseService;

    AgreementDto agreementDto1;
    AgreementDto agreementDto2;
    Agreement agreement1;
    Agreement agreement2;
    UUID uuid;

    @BeforeEach
    void setUp() {
        agreementDto1 = AgreementDto.builder().build();
        agreementDto2 = AgreementDto.builder().build();
        agreement1 = new Agreement();
        agreement2 = new Agreement();
        uuid = UUID.fromString("d358838e-1134-4101-85ac-5d99e8debfae");
    }

    @Test
    void create_success() {
        // given
        when(agreementDTOMapper.mapDtoToEntity(agreementDto1)).thenReturn(agreement1);

        // when
        agreementDatabaseService.create(agreementDto1);

        // then
        verify(agreementDTOMapper).mapDtoToEntity(agreementDto1);
        verify(agreementRepository).save(agreement1);
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        List<AgreementDto> expected = List.of(agreementDto1, agreementDto2);
        List<Agreement> agreements = List.of(agreement1, agreement2);
        when(agreementRepository.findAllNotDeleted()).thenReturn(agreements);
        when(agreementDTOMapper.getDtoList(agreements)).thenReturn(expected);

        // when
        List<AgreementDto> actual = agreementDatabaseService.findAllNotDeleted();

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAllNotDeleted();
        verify(agreementDTOMapper).getDtoList(agreements);
    }

    @Test
    void findAllNotDeleted_emptyListReturned() {
        // given
        when(agreementRepository.findAllNotDeleted()).thenReturn(Collections.emptyList());

        // when
        List<AgreementDto> actual = agreementDatabaseService.findAllNotDeleted();

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void findDeletedAccounts_success() {
        // given
        List<AgreementDto> expected = List.of(agreementDto1, agreementDto2);
        List<Agreement> deletedAgreements = List.of(agreement1, agreement2);
        when(agreementRepository.findAllDeleted()).thenReturn(deletedAgreements);
        when(agreementDTOMapper.getDtoList(deletedAgreements)).thenReturn(expected);

        // when
        List<AgreementDto> actual = agreementDatabaseService.findDeletedAgreements();

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAllDeleted();
        verify(agreementDTOMapper).getDtoList(deletedAgreements);
    }

    @Test
    void findDeletedAgreements_nullListReturned() {
        // given
        when(agreementRepository.findAllDeleted()).thenReturn(null);

        // when
        List<AgreementDto> actual = agreementDatabaseService.findDeletedAgreements();

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_success() {
        // given
        AgreementDto expected = agreementDto1;
        when(agreementRepository.findById(uuid)).thenReturn(Optional.ofNullable(agreement1));
        when(agreementDTOMapper.mapEntityToDto(agreement1)).thenReturn(agreementDto1);

        // when
        AgreementDto actual = agreementDatabaseService.findById(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findById(uuid);
        verify(agreementDTOMapper).mapEntityToDto(agreement1);
    }

    @Test
    void findById_throws_dataNotFoundException() {
        // given
        String strUuid = "d358838e-1134-4101-85ac-5d99e8debfae";
        when(agreementRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> agreementDatabaseService.findById(strUuid));
    }

    @Test
    void findSavingsAgreementByClientId_success() {
        // given
        Agreement expected = agreement1;
        ProductType type = ProductType.SAVINGS_ACCOUNT;
        when(agreementRepository.findAgreementByClientIdAndProductType(uuid, type))
                .thenReturn(Optional.ofNullable(agreement1));

        // when
        Agreement actual = agreementDatabaseService.findSavingsAgreementByClientId(uuid);

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAgreementByClientIdAndProductType(uuid, type);
    }

    @Test
    void update_success() {
        // given
        AgreementDto updatedAgreementDto = agreementDto1;
        Agreement updatedAgreement = agreement1;
        Agreement agreementToUpdate = agreement2;

        when(agreementDTOMapper.mapDtoToEntity(updatedAgreementDto)).thenReturn(updatedAgreement);
        when(agreementRepository.findById(uuid)).thenReturn(Optional.ofNullable(agreementToUpdate));
        when(agreementUpdateService.update(agreementToUpdate, updatedAgreement)).thenReturn(agreement1);


        // when
        agreementDatabaseService.update(String.valueOf(uuid), updatedAgreementDto);

        // then
        verify(agreementDTOMapper).mapDtoToEntity(updatedAgreementDto);
        verify(agreementRepository).findById(uuid);
        verify(agreementUpdateService).update(agreementToUpdate, updatedAgreement);
        verify(agreementRepository).save(agreement1);
    }

    @Test
    void update_throws_dataNotFoundException() {
        // given
        AgreementDto updatedAgreementDto = agreementDto1;
        String strUuid = "d358838e-1134-4101-85ac-5d99e8debfae";

        when(agreementDTOMapper.mapDtoToEntity(updatedAgreementDto)).thenReturn(agreement1);
        when(agreementRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> agreementDatabaseService.update(strUuid, updatedAgreementDto));
    }

    @Test
    void delete_success() {
        // given
        when(agreementRepository.findById(uuid)).thenReturn(Optional.ofNullable(agreement1));

        // when
        agreementDatabaseService.delete(String.valueOf(uuid));

        // then
        verify(agreementRepository).findById(uuid);
        verify(agreementRepository).save(agreement1);
        assertTrue(agreement1.isDeleted());
    }

    @Test
    void findAgreementsByManagerUuid_success() {
        // given
        List<AgreementDto> expected = List.of(agreementDto1, agreementDto2);
        List<Agreement> agreements = List.of(agreement1, agreement2);
        when(agreementRepository.findAgreementsWhereManagerIdIs(uuid)).thenReturn(agreements);
        when(agreementDTOMapper.getDtoList(agreements)).thenReturn(List.of(agreementDto1, agreementDto2));

        // when
        List<AgreementDto> actual = agreementDatabaseService.findAgreementsByManagerUuid(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAgreementsWhereManagerIdIs(uuid);
        verify(agreementDTOMapper).getDtoList(agreements);
    }

    @Test
    void findAgreementDtoListByClientUuid_success() {
        // given
        List<AgreementDto> expected = List.of(agreementDto1, agreementDto2);
        List<Agreement> agreements = List.of(agreement1, agreement2);
        when(agreementRepository.findAgreementsWhereClientIdIs(uuid)).thenReturn(agreements);
        when(agreementWithProductDtoMapper.getDtoList(agreements)).thenReturn(List.of(agreementDto1, agreementDto2));

        // when
        List<AgreementDto> actual = agreementDatabaseService.findAgreementDtoListByClientUuid(String.valueOf(uuid));

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAgreementsWhereClientIdIs(uuid);
        verify(agreementWithProductDtoMapper).getDtoList(agreements);
    }

    @Test
    void findAgreementsByClientUuid_success() {
        // given
        List<Agreement> expected = List.of(agreement1, agreement2);
        when(agreementRepository.findAgreementsWhereClientIdIs(uuid)).thenReturn(List.of(agreement1, agreement2));

        // when
        List<Agreement> actual = agreementDatabaseService.findAgreementsByClientUuid(uuid);

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAgreementsWhereClientIdIs(uuid);
    }
}
