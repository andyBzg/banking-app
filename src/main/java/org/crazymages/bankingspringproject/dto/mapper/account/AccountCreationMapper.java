package org.crazymages.bankingspringproject.dto.mapper.account;

import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AccountCreationMapper implements DtoMapper<Account, AccountDto> {
    @Override
    public AccountDto mapEntityToDto(Account entity) {
        if (entity == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return AccountDto.builder()
                .name(entity.getName())
                .type(entity.getType() != null ? entity.getType().name() : null)
                .currencyCode(entity.getCurrencyCode() != null ? entity.getCurrencyCode().name() : null)
                .build();


    }

    @Override
    public Account mapDtoToEntity(AccountDto entityDto) {
        if (entityDto == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return Account.builder()
                .name(entityDto.getName())
                .type(entityDto.getType() != null ? AccountType.valueOf(entityDto.getType()) : null)
                .currencyCode(entityDto.getCurrencyCode() != null ? CurrencyCode.valueOf(entityDto.getCurrencyCode()) : null)
                .build();
    }

    @Override
    public List<AccountDto> getDtoList(List<Account> entityList) {
        return Optional.ofNullable(entityList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
