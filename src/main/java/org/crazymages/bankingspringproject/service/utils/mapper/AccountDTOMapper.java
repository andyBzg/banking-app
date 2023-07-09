package org.crazymages.bankingspringproject.service.utils.mapper;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.dto.AccountDTO;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Account and AccountDTO objects.
 */
@Component
public class AccountDTOMapper {

    /**
     * Maps an Account object to an AccountDTO object.
     *
     * @param account The Account object to be mapped.
     * @return The mapped AccountDTO object.
     */
    public AccountDTO mapToAccountDTO(Account account) {
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

    /**
     * Maps an AccountDTO object to an Account object.
     *
     * @param accountDTO The AccountDTO object to be mapped.
     * @return The mapped Account object.
     */
    public Account mapToAccount(AccountDTO accountDTO) {
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

    /**
     * Maps a list of Account objects to a list of AccountDTO objects.
     *
     * @param accountList The list of Account objects to be mapped.
     * @return The list of mapped AccountDTO objects.
     * @throws DataNotFoundException If the input accountList is null.
     */
    public List<AccountDTO> getListOfAccountDTOs(List<Account> accountList) {
        return Optional.ofNullable(accountList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapToAccountDTO)
                .toList();
    }
}
