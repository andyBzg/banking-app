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
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setUuid(String.valueOf(transaction.getUuid()));
        transactionDto.setDebitAccountUuid(String.valueOf(transaction.getDebitAccountUuid()));
        transactionDto.setCreditAccountUuid(String.valueOf(transaction.getCreditAccountUuid()));
        transactionDto.setType(String.valueOf(transaction.getType()));
        transactionDto.setCurrencyCode(String.valueOf(transaction.getCurrencyCode()));
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setDescription(transaction.getDescription());
        return transactionDto;
    }

    @Override
    public Transaction mapDtoToEntity(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setUuid(UUID.fromString(transactionDto.getUuid()));
        transaction.setDebitAccountUuid(UUID.fromString(transactionDto.getDebitAccountUuid()));
        transaction.setCreditAccountUuid(UUID.fromString(transactionDto.getCreditAccountUuid()));
        transaction.setType(TransactionType.valueOf(transactionDto.getType()));
        transaction.setCurrencyCode(CurrencyCode.valueOf(transactionDto.getCurrencyCode()));
        transaction.setAmount(transactionDto.getAmount());
        transaction.setDescription(transactionDto.getDescription());
        return transaction;
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
