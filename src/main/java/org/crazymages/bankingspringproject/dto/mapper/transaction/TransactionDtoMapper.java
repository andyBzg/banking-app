package org.crazymages.bankingspringproject.dto.mapper.transaction;

import org.crazymages.bankingspringproject.dto.TransactionDto;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.TransactionType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Component class that provides mapping functionality between Transaction and TransactionDTO objects.
 */
@Component
public class TransactionDtoMapper implements DtoMapper<Transaction, TransactionDto> {

    @Override
    public TransactionDto mapEntityToDto(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("transaction cannot be null");
        }
        return TransactionDto.builder()
                .debitAccountUuid(transaction.getDebitAccountUuid() != null ? transaction.getDebitAccountUuid().toString() : null)
                .creditAccountUuid(transaction.getCreditAccountUuid() != null ? transaction.getCreditAccountUuid().toString() : null)
                .type(transaction.getType() != null ? transaction.getType().name() : null)
                .currencyCode(transaction.getCurrencyCode() != null ? transaction.getCurrencyCode().name() : null)
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .build();
    }

    @Override
    public Transaction mapDtoToEntity(TransactionDto transactionDto) {
        if (transactionDto == null) {
            throw new IllegalArgumentException("transactionDto cannot be null");
        }
        return Transaction.builder()
                .debitAccountUuid(transactionDto.getDebitAccountUuid() != null ? UUID.fromString(transactionDto.getDebitAccountUuid()) : null)
                .creditAccountUuid(transactionDto.getCreditAccountUuid() != null ? UUID.fromString(transactionDto.getCreditAccountUuid()) : null)
                .type(transactionDto.getType() != null ? TransactionType.valueOf(transactionDto.getType()) : null)
                .currencyCode(transactionDto.getCurrencyCode() != null ? CurrencyCode.valueOf(transactionDto.getCurrencyCode()) : null)
                .amount(transactionDto.getAmount())
                .description(transactionDto.getDescription())
                .build();
    }

    @Override
    public List<TransactionDto> getDtoList(List<Transaction> transactionList) {
        return Optional.ofNullable(transactionList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
