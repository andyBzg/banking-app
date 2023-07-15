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
        AccountDto accountDto = new AccountDto();
        accountDto.setUuid(String.valueOf(account.getUuid()));
        accountDto.setClientUuid(String.valueOf(account.getClientUuid()));
        accountDto.setName(account.getName());
        accountDto.setType(String.valueOf(account.getType()));
        accountDto.setStatus(String.valueOf(account.getStatus()));
        accountDto.setBalance(account.getBalance());
        accountDto.setCurrencyCode(String.valueOf(account.getCurrencyCode()));
        return accountDto;
    }

    @Override
    public Account mapDtoToEntity(AccountDto accountDto) {
        Account account = new Account();
        account.setUuid(UUID.fromString(accountDto.getUuid()));
        account.setClientUuid(UUID.fromString(accountDto.getClientUuid()));
        account.setName(accountDto.getName());
        account.setType(AccountType.valueOf(accountDto.getType()));
        account.setStatus(AccountStatus.valueOf(accountDto.getStatus()));
        account.setBalance(accountDto.getBalance());
        account.setCurrencyCode(CurrencyCode.valueOf(accountDto.getCurrencyCode()));
        return account;
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
