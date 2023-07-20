package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Component class that provides mapping functionality between Account and AccountDTO objects.
 */
@Component
public class AccountDtoMapper implements DtoMapper<Account, AccountDto> {

    @Override
    public AccountDto mapEntityToDto(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return AccountDto.builder()
                .clientUuid(String.valueOf(account.getClientUuid()))
                .name(account.getName())
                .type(String.valueOf(account.getType()))
                .status(String.valueOf(account.getStatus()))
                .balance(account.getBalance())
                .currencyCode(String.valueOf(account.getCurrencyCode()))
                .build();
    }

    @Override
    public Account mapDtoToEntity(AccountDto accountDto) {
        if (accountDto == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return Account.builder()
                .clientUuid(UUID.fromString(accountDto.getClientUuid()))
                .name(accountDto.getName())
                .type(AccountType.valueOf(accountDto.getType()))
                .status(AccountStatus.valueOf(accountDto.getStatus()))
                .balance(accountDto.getBalance())
                .currencyCode(CurrencyCode.valueOf(accountDto.getCurrencyCode()))
                .build();
    }

    @Override
    public List<AccountDto> getDtoList(List<Account> accountList) {
        return Optional.ofNullable(accountList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
