package org.crazymages.bankingspringproject.dto.mapper.account;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.dto.AccountDto;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

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
                .clientUuid(account.getClientUuid() != null ? account.getClientUuid().toString() : null)
                .name(account.getName())
                .type(account.getType() != null ? account.getType().name() : null)
                .status(account.getStatus() != null ? account.getStatus().name() : null)
                .balance(account.getBalance())
                .currencyCode(account.getCurrencyCode() != null ? account.getCurrencyCode().name() : null)
                .build();
    }

    @Override
    public Account mapDtoToEntity(AccountDto accountDto) {
        if (accountDto == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return Account.builder()
                .clientUuid(accountDto.getClientUuid() != null ? UUID.fromString(accountDto.getClientUuid()) : null)
                .name(accountDto.getName())
                .type(accountDto.getType() != null ? AccountType.valueOf(accountDto.getType()) : null)
                .status(accountDto.getStatus() != null ? AccountStatus.valueOf(accountDto.getStatus()) : null)
                .balance(accountDto.getBalance())
                .currencyCode(accountDto.getCurrencyCode() != null ? CurrencyCode.valueOf(accountDto.getCurrencyCode()) : null)
                .build();
    }
}
