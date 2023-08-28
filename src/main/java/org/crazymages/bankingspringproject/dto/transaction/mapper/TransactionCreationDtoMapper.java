package org.crazymages.bankingspringproject.dto.transaction.mapper;

import org.crazymages.bankingspringproject.dto.transaction.TransactionDto;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.dto.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionCreationDtoMapper implements DtoMapper<Transaction, TransactionDto> {
    @Override
    public TransactionDto mapEntityToDto(Transaction entity) {
        if (entity == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return TransactionDto.builder()
                .creditAccountUuid(entity.getCreditAccountUuid() != null ? String.valueOf(entity.getCreditAccountUuid()) : null)
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .build();
    }

    @Override
    public Transaction mapDtoToEntity(TransactionDto entityDto) {
        if (entityDto == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return Transaction.builder()
                .creditAccountUuid(entityDto.getCreditAccountUuid() != null ? UUID.fromString(entityDto.getCreditAccountUuid()) : null)
                .amount(entityDto.getAmount())
                .description(entityDto.getDescription())
                .build();
    }
}
