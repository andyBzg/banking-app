package org.crazymages.bankingspringproject.dto.mapper.transaction;

import org.crazymages.bankingspringproject.dto.TransactionDto;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCreationDtoMapperTest {

    TransactionCreationDtoMapper transactionCreationDtoMapper;
    TransactionDto transactionDto;
    Transaction transaction1;

    @BeforeEach
    void setUp() {
        transactionCreationDtoMapper = new TransactionCreationDtoMapper();
        transactionDto = TransactionDto.builder().build();

        transaction1 = new Transaction();
        transaction1.setCreditAccountUuid(UUID.randomUUID());
        transaction1.setAmount(BigDecimal.valueOf(100));
        transaction1.setDescription("Transaction 1");
    }

    @Test
    void mapEntityToDto_validTransaction_success() {
        // when
        TransactionDto transactionDto = transactionCreationDtoMapper.mapEntityToDto(transaction1);

        // then
        assertEquals(transaction1.getCreditAccountUuid().toString(), transactionDto.getCreditAccountUuid());
        assertEquals(transaction1.getAmount(), transactionDto.getAmount());
        assertEquals(transaction1.getDescription(), transactionDto.getDescription());
    }

    @Test
    void mapEntityToDto_missingTransactionProperties_returnsTransactionDtoWithNullProperties() {
        // given
        Transaction transaction = new Transaction();

        // when
        TransactionDto transactionDto = transactionCreationDtoMapper.mapEntityToDto(transaction);

        // then
        assertNull(transactionDto.getCreditAccountUuid());
        assertNull(transactionDto.getAmount());
        assertNull(transactionDto.getDescription());
    }

    @Test
    void mapEntityToDto_nullTransaction_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionCreationDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validTransactionDto_success() {
        // given
        transactionDto.setCreditAccountUuid("3be3e176-3215-4d57-92c3-3f5b0e06c6c4");
        transactionDto.setAmount(BigDecimal.valueOf(100));
        transactionDto.setDescription("Transaction 1");

        // when
        Transaction transaction = transactionCreationDtoMapper.mapDtoToEntity(transactionDto);

        // then
        assertEquals(UUID.fromString(transactionDto.getCreditAccountUuid()), transaction.getCreditAccountUuid());
        assertEquals(transactionDto.getAmount(), transaction.getAmount());
        assertEquals(transactionDto.getDescription(), transaction.getDescription());
    }

    @Test
    void mapDtoToEntity_nullTransactionDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> transactionCreationDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingTransactionDtoProperties_returnsTransactionWithNullProperties() {
        // given
        TransactionDto transactionDto = TransactionDto.builder().build();

        // when
        Transaction transaction = transactionCreationDtoMapper.mapDtoToEntity(transactionDto);

        // then
        assertNull(transaction.getCreditAccountUuid());
        assertNull(transaction.getAmount());
        assertNull(transaction.getDescription());
    }
}
