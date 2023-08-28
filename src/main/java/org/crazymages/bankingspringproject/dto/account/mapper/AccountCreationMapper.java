package org.crazymages.bankingspringproject.dto.account.mapper;

import org.crazymages.bankingspringproject.dto.account.AccountCreationDto;
import org.crazymages.bankingspringproject.dto.DtoMapper;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.springframework.stereotype.Component;

@Component
public class AccountCreationMapper implements DtoMapper<Account, AccountCreationDto> {
    @Override
    public AccountCreationDto mapEntityToDto(Account entity) {
        if (entity == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return AccountCreationDto.builder()
                .name(entity.getName())
                .type(entity.getType() != null ? entity.getType().name() : null)
                .currencyCode(entity.getCurrencyCode() != null ? entity.getCurrencyCode().name() : null)
                .build();
    }

    @Override
    public Account mapDtoToEntity(AccountCreationDto entityDto) {
        if (entityDto == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return Account.builder()
                .name(entityDto.getName())
                .type(entityDto.getType() != null ? AccountType.valueOf(entityDto.getType()) : null)
                .currencyCode(entityDto.getCurrencyCode() != null ? CurrencyCode.valueOf(entityDto.getCurrencyCode()) : null)
                .build();
    }
}
