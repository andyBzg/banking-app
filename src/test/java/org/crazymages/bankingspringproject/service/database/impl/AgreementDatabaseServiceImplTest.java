package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.AgreementDTO;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.repository.AgreementRepository;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.AgreementDTOMapper;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AgreementDatabaseServiceImplTest {

    @Mock
    AgreementRepository agreementRepository;
    @Mock
    EntityUpdateService<Agreement> agreementUpdateService;
    @Mock
    AgreementDTOMapper agreementDTOMapper;

    @InjectMocks
    AgreementDatabaseServiceImpl agreementDatabaseService;

    AgreementDTO agreementDTO1;
    AgreementDTO agreementDTO2;
    Agreement agreement1;
    Agreement agreement2;
    UUID uuid;

    @BeforeEach
    void setUp() {
        agreementDTO1 = new AgreementDTO();
        agreementDTO2 = new AgreementDTO();
        agreement1 = new Agreement();
        agreement2 = new Agreement();
        uuid = UUID.randomUUID();
    }

    @Test
    void create_success() {
        // given
        when(agreementDTOMapper.mapDtoToEntity(agreementDTO1)).thenReturn(agreement1);

        // when
        agreementDatabaseService.create(agreementDTO1);

        // then
        verify(agreementDTOMapper).mapDtoToEntity(agreementDTO1);
        verify(agreementRepository).save(agreement1);
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        List<AgreementDTO> expected = List.of(agreementDTO1, agreementDTO2);
        List<Agreement> agreements = List.of(agreement1, agreement2);
        when(agreementRepository.findAllNotDeleted()).thenReturn(agreements);
        when(agreementDTOMapper.getListOfDTOs(agreements)).thenReturn(expected);

        // when
        List<AgreementDTO> actual = agreementDatabaseService.findAllNotDeleted();

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAllNotDeleted();
        verify(agreementDTOMapper).getListOfDTOs(agreements);
    }

    @Test
    void findDeletedAccounts_success() {
        // given
        List<AgreementDTO> expected = List.of(agreementDTO1, agreementDTO2);
        List<Agreement> deletedAgreements = List.of(agreement1, agreement2);
        when(agreementRepository.findAllDeleted()).thenReturn(deletedAgreements);
        when(agreementDTOMapper.getListOfDTOs(deletedAgreements)).thenReturn(expected);

        // when
        List<AgreementDTO> actual = agreementDatabaseService.findDeletedAgreements();

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAllDeleted();
        verify(agreementDTOMapper).getListOfDTOs(deletedAgreements);
    }

    @Test
    void findById_success() {
        // given
        AgreementDTO expected = agreementDTO1;
        when(agreementRepository.findById(uuid)).thenReturn(Optional.ofNullable(agreement1));
        when(agreementDTOMapper.mapEntityToDto(agreement1)).thenReturn(agreementDTO1);

        // when
        AgreementDTO actual = agreementDatabaseService.findById(uuid);

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findById(uuid);
        verify(agreementDTOMapper).mapEntityToDto(agreement1);
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
        AgreementDTO updatedAgreementDTO = agreementDTO1;
        Agreement updatedAgreement = agreement1;
        Agreement agreementToUpdate = agreement2;

        when(agreementDTOMapper.mapDtoToEntity(updatedAgreementDTO)).thenReturn(updatedAgreement);
        when(agreementRepository.findById(uuid)).thenReturn(Optional.ofNullable(agreementToUpdate));
        when(agreementUpdateService.update(agreementToUpdate, updatedAgreement)).thenReturn(agreement1);


        // when
        agreementDatabaseService.update(uuid, updatedAgreementDTO);

        // then
        verify(agreementDTOMapper).mapDtoToEntity(updatedAgreementDTO);
        verify(agreementRepository).findById(uuid);
        verify(agreementUpdateService).update(agreementToUpdate, updatedAgreement);
        verify(agreementRepository).save(agreement1);
    }

    @Test
    void delete_success() {
        // given
        when(agreementRepository.findById(uuid)).thenReturn(Optional.ofNullable(agreement1));

        // when
        agreementDatabaseService.delete(uuid);

        // then
        verify(agreementRepository).findById(uuid);
        verify(agreementRepository).save(agreement1);
        assertTrue(agreement1.isDeleted());
    }

    @Test
    void findAgreementsByManagerUuid_success() {
        // given
        List<AgreementDTO> expected = List.of(agreementDTO1, agreementDTO2);
        List<Agreement> agreements = List.of(agreement1, agreement2);
        when(agreementRepository.findAgreementsWhereManagerIdIs(uuid)).thenReturn(agreements);
        when(agreementDTOMapper.getListOfDTOs(agreements)).thenReturn(List.of(agreementDTO1, agreementDTO2));

        // when
        List<AgreementDTO> actual = agreementDatabaseService.findAgreementsByManagerUuid(uuid);

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAgreementsWhereManagerIdIs(uuid);
        verify(agreementDTOMapper).getListOfDTOs(agreements);
    }

    @Test
    void findAgreementDTOsByClientUuid_success() {
        // given
        List<AgreementDTO> expected = List.of(agreementDTO1, agreementDTO2);
        List<Agreement> agreements = List.of(agreement1, agreement2);
        when(agreementRepository.findAgreementsWhereClientIdIs(uuid)).thenReturn(agreements);
        when(agreementDTOMapper.getListOfDTOs(agreements)).thenReturn(List.of(agreementDTO1, agreementDTO2));

        // when
        List<AgreementDTO> actual = agreementDatabaseService.findAgreementDTOsByClientUuid(uuid);

        // then
        assertEquals(expected, actual);
        verify(agreementRepository).findAgreementsWhereClientIdIs(uuid);
        verify(agreementDTOMapper).getListOfDTOs(agreements);
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