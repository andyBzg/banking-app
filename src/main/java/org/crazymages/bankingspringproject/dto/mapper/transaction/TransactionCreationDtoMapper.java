package org.crazymages.bankingspringproject.dto.mapper.transaction;

import org.crazymages.bankingspringproject.dto.TransactionDto;
import org.crazymages.bankingspringproject.entity.Transaction;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    @Override
    public List<TransactionDto> getDtoList(List<Transaction> entityList) {
        return Optional.ofNullable(entityList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
