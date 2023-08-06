package org.crazymages.bankingspringproject.service.utils.initializer.impl;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AgreementInitializerImplTest {

    AgreementInitializerImpl agreementInitializer;
    UUID accountUuid;
    Product product;

    @BeforeEach
    void SetUp() {
        agreementInitializer = new AgreementInitializerImpl();
        accountUuid = UUID.randomUUID();
        product = new Product();
        product.setUuid(accountUuid);
        product.setInterestRate(BigDecimal.valueOf(0.05));
    }

    @Test
    void initializeAgreement_createsAgreementWithCorrectProperties() {
        // when
        Agreement agreement = agreementInitializer.initializeAgreement(accountUuid, product);

        // then
        assertEquals(accountUuid, agreement.getAccountUuid());
        assertEquals(product.getUuid(), agreement.getProductUuid());
        assertEquals(product.getInterestRate(), agreement.getInterestRate());
        assertEquals(AgreementStatus.PENDING, agreement.getStatus());
    }

    @Test
    void initializeAgreement_returnsNewAgreementInstance() {
        // when
        Agreement agreement1 = agreementInitializer.initializeAgreement(accountUuid, product);
        Agreement agreement2 = agreementInitializer.initializeAgreement(accountUuid, product);

        // then
        assertNotSame(agreement1, agreement2);
    }

    @Test
    void initializeAgreement_withNullAccountUuid_returnsAgreementWithNullAccountId() {
        // when
        Agreement agreement = agreementInitializer.initializeAgreement(null, product);

        // then
        assertNull(agreement.getAccountUuid());
    }

    @Test
    void initializeAgreement_productPropertiesAreNull_returnsAgreementWithNullProperties() {
        // given
        product.setUuid(null);
        product.setInterestRate(null);

        // when
        Agreement agreement = agreementInitializer.initializeAgreement(accountUuid, product);

        // then
        assertNull(agreement.getProductUuid());
        assertNull(agreement.getInterestRate());
    }

    @Test
    void initializeAgreement_withNullProduct_throwsIllegalArgumentException() {
        // when, then
        assertThrows(IllegalArgumentException.class,
                () -> agreementInitializer.initializeAgreement(accountUuid, null));
    }
}
