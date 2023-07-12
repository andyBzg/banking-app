package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.dto.AccountDTO;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Account and AccountDTO objects.
 */
@Component
public class AccountDTOMapper implements DTOMapper<Account, AccountDTO> {

    @Override
    public AccountDTO mapEntityToDto(Account account) {
        return new AccountDTO(
                account.getUuid(),
                account.getClientUuid(),
                account.getName(),
                account.getType(),
                account.getStatus(),
                account.getBalance(),
                account.getCurrencyCode()
        );
    }

    @Override
    public Account mapDtoToEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setUuid(accountDTO.getUuid());
        account.setClientUuid(accountDTO.getClientUuid());
        account.setName(accountDTO.getName());
        account.setType(accountDTO.getType());
        account.setStatus(accountDTO.getStatus());
        account.setBalance(accountDTO.getBalance());
        account.setCurrencyCode(accountDTO.getCurrencyCode());
        return account;
    }

    @Override
    public List<AccountDTO> getListOfDTOs(List<Account> accountList) {
        return Optional.ofNullable(accountList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
