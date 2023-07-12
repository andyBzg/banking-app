package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.TransactionDTO;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Transaction and TransactionDTO objects.
 */
@Component
public class TransactionDTOMapper implements DTOMapper<Transaction, TransactionDTO> {

    @Override
    public TransactionDTO mapEntityToDto(Transaction transaction) {
        return new TransactionDTO(
                transaction.getUuid(),
                transaction.getDebitAccountUuid(),
                transaction.getCreditAccountUuid(),
                transaction.getType(),
                transaction.getCurrencyCode(),
                transaction.getAmount(),
                transaction.getDescription()
        );
    }

    @Override
    public Transaction mapDtoToEntity(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setUuid(transactionDTO.getUuid());
        transaction.setDebitAccountUuid(transactionDTO.getDebitAccountUuid());
        transaction.setCreditAccountUuid(transactionDTO.getCreditAccountUuid());
        transaction.setType(transactionDTO.getType());
        transaction.setCurrencyCode(transactionDTO.getCurrencyCode());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        return transaction;
    }

    @Override
    public List<TransactionDTO> getListOfDTOs(List<Transaction> transactionList) {
        return Optional.ofNullable(transactionList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
