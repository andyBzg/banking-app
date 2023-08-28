package org.crazymages.bankingspringproject.dto.account.mapper;

import org.crazymages.bankingspringproject.dto.account.AccountDto;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.dto.DtoMapper;
import org.springframework.stereotype.Component;

@Component
public class AccountUpdateMapper implements DtoMapper<Account, AccountDto> {
    @Override
    public AccountDto mapEntityToDto(Account entity) {
        if (entity == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return AccountDto.builder()
                .name(entity.getName())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .build();
    }

    @Override
    public Account mapDtoToEntity(AccountDto entityDto) {
        if (entityDto == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return Account.builder()
                .name(entityDto.getName())
                .status(entityDto.getStatus() != null ? AccountStatus.valueOf(entityDto.getStatus()) : null)
                .build();
    }
}
