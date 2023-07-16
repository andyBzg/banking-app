package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.AgreementDto;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AgreementDtoMapperTest {

    AgreementDtoMapper agreementDtoMapper;
    AgreementDto agreementDto;
    Agreement agreement1;
    Agreement agreement2;

    @BeforeEach
    void setUp() {
        agreementDtoMapper = new AgreementDtoMapper();
        agreementDto = new AgreementDto();

        agreement1 = new Agreement();
        agreement1.setUuid(UUID.randomUUID());
        agreement1.setAccountUuid(UUID.randomUUID());
        agreement1.setProductUuid(UUID.randomUUID());
        agreement1.setInterestRate(BigDecimal.valueOf(0.05));
        agreement1.setStatus(AgreementStatus.ACTIVE);
        agreement1.setAmount(BigDecimal.valueOf(1000));

        agreement2 = new Agreement();
        agreement2.setUuid(UUID.randomUUID());
        agreement2.setAccountUuid(UUID.randomUUID());
        agreement2.setProductUuid(UUID.randomUUID());
        agreement2.setInterestRate(BigDecimal.valueOf(0.1));
        agreement2.setStatus(AgreementStatus.PENDING);
        agreement2.setAmount(BigDecimal.valueOf(2000));
    }

    @Test
    void mapEntityToDto_validAgreement_success() {
        // when
        AgreementDto agreementDto = agreementDtoMapper.mapEntityToDto(agreement1);

        // then
        assertEquals(agreement1.getUuid().toString(), agreementDto.getUuid());
        assertEquals(agreement1.getAccountUuid().toString(), agreementDto.getAccountUuid());
        assertEquals(agreement1.getProductUuid().toString(), agreementDto.getProductUuid());
        assertEquals(agreement1.getInterestRate(), agreementDto.getInterestRate());
        assertEquals(agreement1.getStatus().toString(), agreementDto.getStatus());
        assertEquals(agreement1.getAmount(), agreementDto.getAmount());
    }

    @Test
    void mapEntityToDto_nullAgreement_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> agreementDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validAgreementDto_success() {
        // given
        agreementDto.setUuid("30348dce-45f7-4e19-aa08-3ed77a8f7ac3");
        agreementDto.setAccountUuid("f59f83b7-9f9b-495b-83e7-09c11856e6a5");
        agreementDto.setProductUuid("2c8ad8ae-9f07-4de5-a2fd-b474c119e4a6");
        agreementDto.setInterestRate(BigDecimal.valueOf(0.08));
        agreementDto.setStatus("ACTIVE");
        agreementDto.setAmount(BigDecimal.valueOf(1500));

        // when
        Agreement agreement = agreementDtoMapper.mapDtoToEntity(agreementDto);

        // then
        assertEquals(UUID.fromString(agreementDto.getUuid()), agreement.getUuid());
        assertEquals(UUID.fromString(agreementDto.getAccountUuid()), agreement.getAccountUuid());
        assertEquals(UUID.fromString(agreementDto.getProductUuid()), agreement.getProductUuid());
        assertEquals(agreementDto.getInterestRate(), agreement.getInterestRate());
        assertEquals(AgreementStatus.valueOf(agreementDto.getStatus()), agreement.getStatus());
        assertEquals(agreementDto.getAmount(), agreement.getAmount());
    }

    @Test
    void mapDtoToEntity_nullAgreementDto_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> agreementDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingAgreementDtoProperties_throwsIllegalArgumentException() {
        // given
        agreementDto.setUuid("30348dce-45f7-4e19-aa08-3ed77a8f7ac3");
        agreementDto.setAccountUuid("f59f83b7-9f9b-495b-83e7-09c11856e6a5");

        // when, then
        assertThrows(NullPointerException.class, () -> agreementDtoMapper.mapDtoToEntity(agreementDto));
    }

    @Test
    void getDtoList_validAgreementList_success() {
        // given
        List<Agreement> agreementList = List.of(agreement1, agreement2);

        // when
        List<AgreementDto> actual = agreementDtoMapper.getDtoList(agreementList);

        // then
        assertEquals(2, actual.size());

        AgreementDto agreementDto1 = actual.get(0);
        assertEquals(agreement1.getUuid().toString(), agreementDto1.getUuid());
        assertEquals(agreement1.getAccountUuid().toString(), agreementDto1.getAccountUuid());
        assertEquals(agreement1.getProductUuid().toString(), agreementDto1.getProductUuid());
        assertEquals(agreement1.getInterestRate(), agreementDto1.getInterestRate());
        assertEquals(agreement1.getStatus().toString(), agreementDto1.getStatus());
        assertEquals(agreement1.getAmount(), agreementDto1.getAmount());

        AgreementDto agreementDto2 = actual.get(1);
        assertEquals(agreement2.getUuid().toString(), agreementDto2.getUuid());
        assertEquals(agreement2.getAccountUuid().toString(), agreementDto2.getAccountUuid());
        assertEquals(agreement2.getProductUuid().toString(), agreementDto2.getProductUuid());
        assertEquals(agreement2.getInterestRate(), agreementDto2.getInterestRate());
        assertEquals(agreement2.getStatus().toString(), agreementDto2.getStatus());
        assertEquals(agreement2.getAmount(), agreementDto2.getAmount());
    }

    @Test
    void getDtoList_nullAgreementList_throwsDataNotFoundException() {
        assertThrows(DataNotFoundException.class, () -> agreementDtoMapper.getDtoList(null));
    }

    @Test
    void getDtoList_emptyAgreementList_returnsEmptyList() {
        // given
        List<Agreement> agreementList = Collections.emptyList();

        // when
        List<AgreementDto> actual = agreementDtoMapper.getDtoList(agreementList);

        // then
        assertTrue(actual.isEmpty());
    }
}
