package org.crazymages.bankingspringproject.service.utils.mapper;

import org.crazymages.bankingspringproject.dto.TransactionDTO;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Transaction and TransactionDTO objects.
 */
@Component
public class TransactionDTOMapper {

    /**
     * Maps a Transaction object to a TransactionDTO object.
     *
     * @param transaction The Transaction object to be mapped.
     * @return The mapped TransactionDTO object.
     */
    public TransactionDTO mapToTransactionDTO(Transaction transaction) {
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

    /**
     * Maps an TransactionDTO object to a Transaction object.
     *
     * @param transactionDTO The TransactionDTO object to be mapped.
     * @return The mapped Transaction object.
     */
    public Transaction mapToTransaction(TransactionDTO transactionDTO) {
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

    /**
     * Maps a list of Transaction objects to a list of TransactionDTO objects.
     *
     * @param transactionList The list of Transaction objects to be mapped.
     * @return The list of mapped TransactionDTO objects.
     * @throws DataNotFoundException If the input transactionList is null.
     */
    public List<TransactionDTO> getListOfTransactionDTOs(List<Transaction> transactionList) {
        return Optional.ofNullable(transactionList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapToTransactionDTO)
                .toList();
    }
}
