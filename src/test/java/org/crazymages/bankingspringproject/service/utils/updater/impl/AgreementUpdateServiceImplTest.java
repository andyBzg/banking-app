package org.crazymages.bankingspringproject.service.utils.updater.impl;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AgreementUpdateServiceImplTest {

    AgreementUpdateServiceImpl agreementUpdateService;
    Agreement agreement;

    @BeforeEach
    void setUp() {
        agreementUpdateService = new AgreementUpdateServiceImpl();
        agreement = new Agreement();
        agreement.setAccountUuid(UUID.randomUUID());
        agreement.setProductUuid(UUID.randomUUID());
        agreement.setInterestRate(BigDecimal.valueOf(0.05));
        agreement.setStatus(AgreementStatus.ACTIVE);
        agreement.setAmount(BigDecimal.valueOf(1000));
    }

    @Test
    void update_withValidFields_updatesAgreementProperties() {
        // given
        Agreement agreementUpdate = new Agreement();
        agreementUpdate.setAccountUuid(UUID.randomUUID());
        agreementUpdate.setProductUuid(UUID.randomUUID());
        agreementUpdate.setInterestRate(BigDecimal.valueOf(0.1));
        agreementUpdate.setStatus(AgreementStatus.PENDING);
        agreementUpdate.setAmount(BigDecimal.valueOf(2000));

        // when
        Agreement actual = agreementUpdateService.update(agreement, agreementUpdate);

        // then
        assertEquals(agreementUpdate.getAccountUuid(), actual.getAccountUuid());
        assertEquals(agreementUpdate.getProductUuid(), actual.getProductUuid());
        assertEquals(agreementUpdate.getInterestRate(), actual.getInterestRate());
        assertEquals(agreementUpdate.getStatus(), actual.getStatus());
        assertEquals(agreementUpdate.getAmount(), actual.getAmount());
    }

    @Test
    void updateProperties_agreementUpdateWithNullFields_updatesOnlyExistentProperties() {
        // given
        Agreement agreementUpdate = new Agreement();
        agreementUpdate.setAccountUuid(null);
        agreementUpdate.setProductUuid(UUID.randomUUID());
        agreementUpdate.setInterestRate(BigDecimal.valueOf(0.1));
        agreementUpdate.setStatus(null);
        agreementUpdate.setAmount(null);

        // when
        Agreement actual = agreementUpdateService.updateProperties(agreement, agreementUpdate);

        // then
        assertNotEquals(agreementUpdate.getAccountUuid(), actual.getAccountUuid());
        assertEquals(agreementUpdate.getProductUuid(), actual.getProductUuid());
        assertEquals(agreementUpdate.getInterestRate(), actual.getInterestRate());
        assertNotEquals(agreementUpdate.getStatus(), actual.getStatus());
        assertNotEquals(agreementUpdate.getAmount(), actual.getAmount());
    }

    @Test
    void update_withNullAgreement_doesNotUpdateAgreementProperties_returnsNull() {
        // given
        Agreement agreementUpdate = new Agreement();
        agreementUpdate.setAccountUuid(UUID.randomUUID());
        agreementUpdate.setProductUuid(UUID.randomUUID());

        // when
        Agreement actual = agreementUpdateService.update(null, agreementUpdate);

        // then
        assertNull(actual);
    }

    @Test
    void update_withNullAgreementUpdate_doesNotUpdateAgreementProperties() {
        // given
        Agreement agreementUpdate = null;

        // when
        Agreement actual = agreementUpdateService.update(agreement, agreementUpdate);

        // then
        assertEquals(agreement.getAccountUuid(), actual.getAccountUuid());
        assertEquals(agreement.getProductUuid(), actual.getProductUuid());
        assertEquals(agreement.getInterestRate(), actual.getInterestRate());
        assertEquals(agreement.getStatus(), actual.getStatus());
        assertEquals(agreement.getAmount(), actual.getAmount());
    }

    @Test
    void updateProperties_withNullAgreementUpdate_throwsNullPointerException() {
        // given
        Agreement agreementUpdate = null;

        // when, then
        assertThrows(NullPointerException.class, () -> agreementUpdateService.updateProperties(agreement, agreementUpdate));
    }
}
