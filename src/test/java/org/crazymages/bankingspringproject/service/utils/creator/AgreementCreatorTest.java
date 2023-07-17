package org.crazymages.bankingspringproject.service.utils.creator;

import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.Product;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AgreementCreatorTest {

    AgreementCreator agreementCreator;
    UUID accountUuid;
    Product product;

    @BeforeEach
    void SetUp() {
        agreementCreator = new AgreementCreator();
        accountUuid = UUID.randomUUID();
        product = new Product();
        product.setUuid(accountUuid);
        product.setInterestRate(BigDecimal.valueOf(0.05));
    }

    @Test
    void apply_createsAgreementWithCorrectProperties() {
        // when
        Agreement agreement = agreementCreator.apply(accountUuid, product);

        // then
        assertEquals(accountUuid, agreement.getAccountUuid());
        assertEquals(product.getUuid(), agreement.getProductUuid());
        assertEquals(product.getInterestRate(), agreement.getInterestRate());
        assertEquals(AgreementStatus.PENDING, agreement.getStatus());
    }

    @Test
    void apply_returnsNewAgreementInstance() {
        // when
        Agreement agreement1 = agreementCreator.apply(accountUuid, product);
        Agreement agreement2 = agreementCreator.apply(accountUuid, product);

        // then
        assertNotSame(agreement1, agreement2);
    }

    @Test
    void apply_withNullAccountUuid_returnsAgreementWithNullAccountId() {
        // when
        Agreement agreement = agreementCreator.apply(null, product);

        // then
        assertNull(agreement.getAccountUuid());
    }

    @Test
    void apply_withNullProduct_throwsNullPointerException() {
        // when, then
        assertThrows(NullPointerException.class, () -> agreementCreator.apply(accountUuid, null));
    }
}
