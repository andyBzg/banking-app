package org.crazymages.bankingspringproject.dto.transaction.mapper;

import org.crazymages.bankingspringproject.dto.transaction.TransactionDto;
import org.crazymages.bankingspringproject.dto.transaction.mapper.TransactionDtoMapper;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDtoMapperTest {

    TransactionDtoMapper transactionDtoMapper;
    TransactionDto transactionDto;
    Transaction transaction1;
    Transaction transaction2;

    @BeforeEach
    void setUp() {
        transactionDtoMapper = new TransactionDtoMapper();
        transactionDto = TransactionDto.builder().build();

        transaction1 = new Transaction();
        transaction1.setUuid(UUID.randomUUID());
        transaction1.setDebitAccountUuid(UUID.randomUUID());
        transaction1.setCreditAccountUuid(UUID.randomUUID());
        transaction1.setType(TransactionType.TRANSFER);
        transaction1.setCurrencyCode(CurrencyCode.EUR);
        transaction1.setAmount(BigDecimal.valueOf(100));
        transaction1.setDescription("Transaction 1");

        transaction2 = new Transaction();
        transaction2.setUuid(UUID.randomUUID());
        transaction2.setDebitAccountUuid(UUID.randomUUID());
        transaction2.setCreditAccountUuid(UUID.randomUUID());
        transaction2.setType(TransactionType.REFUND);
        transaction2.setCurrencyCode(CurrencyCode.USD);
        transaction2.setAmount(BigDecimal.valueOf(200));
        transaction2.setDescription("Transaction 2");
    }

    @Test
    void mapEntityToDto_validTransaction_success() {
        // when
        TransactionDto transactionDto = transactionDtoMapper.mapEntityToDto(transaction1);

        // then
        assertEquals(transaction1.getDebitAccountUuid().toString(), transactionDto.getDebitAccountUuid());
        assertEquals(transaction1.getCreditAccountUuid().toString(), transactionDto.getCreditAccountUuid());
        assertEquals(transaction1.getType().toString(), transactionDto.getType());
        assertEquals(transaction1.getCurrencyCode().toString(), transactionDto.getCurrencyCode());
        assertEquals(transaction1.getAmount(), transactionDto.getAmount());
        assertEquals(transaction1.getDescription(), transactionDto.getDescription());
    }

    @Test
    void mapEntityToDto_missingTransactionProperties_returnsTransactionDtoWithNullProperties() {
        // given
        Transaction transaction = new Transaction();

        // when
        TransactionDto transactionDto = transactionDtoMapper.mapEntityToDto(transaction);

        // then
        assertNull(transactionDto.getDebitAccountUuid());
        assertNull(transactionDto.getCreditAccountUuid());
        assertNull(transactionDto.getType());
        assertNull(transactionDto.getCurrencyCode());
        assertNull(transactionDto.getAmount());
        assertNull(transactionDto.getDescription());
    }

    @Test
    void mapEntityToDto_nullTransaction_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validTransactionDto_success() {
        // given
        transactionDto.setDebitAccountUuid("f59f83b7-9f9b-495b-83e7-09c11856e6a5");
        transactionDto.setCreditAccountUuid("3be3e176-3215-4d57-92c3-3f5b0e06c6c4");
        transactionDto.setType("TRANSFER");
        transactionDto.setCurrencyCode("EUR");
        transactionDto.setAmount(BigDecimal.valueOf(100));
        transactionDto.setDescription("Transaction 1");

        // when
        Transaction transaction = transactionDtoMapper.mapDtoToEntity(transactionDto);

        // then
        assertEquals(UUID.fromString(transactionDto.getDebitAccountUuid()), transaction.getDebitAccountUuid());
        assertEquals(UUID.fromString(transactionDto.getCreditAccountUuid()), transaction.getCreditAccountUuid());
        assertEquals(TransactionType.valueOf(transactionDto.getType()), transaction.getType());
        assertEquals(CurrencyCode.valueOf(transactionDto.getCurrencyCode()), transaction.getCurrencyCode());
        assertEquals(transactionDto.getAmount(), transaction.getAmount());
        assertEquals(transactionDto.getDescription(), transaction.getDescription());
    }

    @Test
    void mapDtoToEntity_nullTransactionDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingTransactionDtoProperties_returnsTransactionWithNullProperties() {
        // given
        TransactionDto transactionDto = TransactionDto.builder().build();

        // when
        Transaction transaction = transactionDtoMapper.mapDtoToEntity(transactionDto);

        // then
        assertNull(transaction.getDebitAccountUuid());
        assertNull(transaction.getCreditAccountUuid());
        assertNull(transaction.getType());
        assertNull(transaction.getCurrencyCode());
        assertNull(transaction.getAmount());
        assertNull(transaction.getDescription());
    }
}
