package org.crazymages.bankingspringproject.dto.agreement.mapper;

import org.crazymages.bankingspringproject.dto.agreement.AgreementDto;
import org.crazymages.bankingspringproject.dto.agreement.mapper.AgreementWithProductDtoMapper;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AgreementWithProductDtoMapperTest {

    AgreementWithProductDtoMapper agreementWithProductDtoMapper;
    AgreementDto agreementDto;
    Agreement agreement1;

    @BeforeEach
    void setUp() {
        agreementWithProductDtoMapper = new AgreementWithProductDtoMapper();
        agreementDto = AgreementDto.builder().build();

        agreement1 = new Agreement();
        agreement1.setInterestRate(BigDecimal.valueOf(0.05));
        agreement1.setStatus(AgreementStatus.ACTIVE);
        agreement1.setAmount(BigDecimal.valueOf(1000));
    }

    @Test
    void mapEntityToDto_validAgreement_success() {
        // when
        AgreementDto agreementDto = agreementWithProductDtoMapper.mapEntityToDto(agreement1);

        // then
        assertEquals(agreement1.getInterestRate(), agreementDto.getInterestRate());
        assertEquals(agreement1.getStatus().toString(), agreementDto.getStatus());
        assertEquals(agreement1.getAmount(), agreementDto.getAmount());
    }

    @Test
    void mapEntityToDto_nullAgreement_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> agreementWithProductDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapEntityToDto_missingAgreementProperties_returnsAgreementDtoWithNullProperties() {
        // given
        Agreement agreement = new Agreement();

        // when
        AgreementDto agreementDto = agreementWithProductDtoMapper.mapEntityToDto(agreement);

        // then
        assertEquals(BigDecimal.ZERO, agreementDto.getInterestRate());
        assertNull(agreementDto.getStatus());
        assertEquals(BigDecimal.ZERO, agreementDto.getAmount());
    }

    @Test
    void mapDtoToEntity_validAgreementDto_success() {
        // given
        agreementDto.setInterestRate(BigDecimal.valueOf(0.08));
        agreementDto.setStatus("ACTIVE");
        agreementDto.setAmount(BigDecimal.valueOf(1500));

        // when
        Agreement agreement = agreementWithProductDtoMapper.mapDtoToEntity(agreementDto);

        // then
        assertFalse(agreement1.isDeleted());
        assertEquals(agreementDto.getInterestRate(), agreement.getInterestRate());
        assertEquals(AgreementStatus.valueOf(agreementDto.getStatus()), agreement.getStatus());
        assertEquals(agreementDto.getAmount(), agreement.getAmount());
    }

    @Test
    void mapDtoToEntity_nullAgreementDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> agreementWithProductDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingAgreementDtoProperties_returnsAgreementWithNullProperties() {
        // given
        AgreementDto agreementDto = AgreementDto.builder().build();

        // when
        Agreement agreement = agreementWithProductDtoMapper.mapDtoToEntity(agreementDto);

        // then
        assertFalse(agreement.isDeleted());
        assertNull(agreement.getInterestRate());
        assertNull(agreement.getStatus());
        assertNull(agreement.getAmount());
    }
}
