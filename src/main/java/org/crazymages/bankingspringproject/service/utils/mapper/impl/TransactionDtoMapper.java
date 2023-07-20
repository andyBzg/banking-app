package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.TransactionDto;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.TransactionType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DtoMapper;
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
                .debitAccountUuid(String.valueOf(transaction.getDebitAccountUuid()))
                .creditAccountUuid(String.valueOf(transaction.getCreditAccountUuid()))
                .type(String.valueOf(transaction.getType()))
                .currencyCode(String.valueOf(transaction.getCurrencyCode()))
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
                .debitAccountUuid(UUID.fromString(transactionDto.getDebitAccountUuid()))
                .creditAccountUuid(UUID.fromString(transactionDto.getCreditAccountUuid()))
                .type(TransactionType.valueOf(transactionDto.getType()))
                .currencyCode(CurrencyCode.valueOf(transactionDto.getCurrencyCode()))
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
