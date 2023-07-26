package org.crazymages.bankingspringproject.repository;

import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;


    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    private void flushAndClearEntityManager() {
        entityManager.flush();
        entityManager.clear();
    }


    @Test
    void findAccountsByStatus_shouldReturnActiveAccounts_success() {
        // given
        AccountStatus status = AccountStatus.ACTIVE;

        Account account1 = new Account();
        account1.setStatus(status);
        accountRepository.save(account1);

        Account account2 = new Account();
        account2.setStatus(AccountStatus.BLOCKED);
        accountRepository.save(account2);

        // when
        List<Account> actual = accountRepository.findAccountsByStatus(status);

        // then
        assertEquals(1, actual.size());
        assertEquals(status, actual.get(0).getStatus());
    }

    @Test
    void findAccountsByStatus_noActiveAccounts_returnsEmptyList_ok() {
        // given
        AccountStatus status = AccountStatus.ACTIVE;

        Account account1 = new Account();
        account1.setStatus(AccountStatus.PENDING);
        accountRepository.save(account1);

        Account account2 = new Account();
        account2.setStatus(AccountStatus.OVERDUE);
        accountRepository.save(account2);

        // when
        List<Account> actual = accountRepository.findAccountsByStatus(status);

        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    void blockAccountsByClientUuid_success() {
        // given
        UUID clientUuid = UUID.fromString("f0621a3c-6849-4ef5-8fc2-7bf7dd450d26");

        Account account1 = new Account();
        account1.setClientUuid(clientUuid);
        account1.setStatus(AccountStatus.ACTIVE);
        entityManager.persist(account1);

        Account account2 = new Account();
        account2.setClientUuid(clientUuid);
        account2.setStatus(AccountStatus.ACTIVE);
        entityManager.persist(account2);

        // when
        accountRepository.blockAccountsByClientUuid(clientUuid);

        // then
        flushAndClearEntityManager();

        Account expectedAccount1 = entityManager.find(Account.class, account1.getUuid());
        Account expectedAccount2 = entityManager.find(Account.class, account2.getUuid());

        assertEquals(AccountStatus.BLOCKED, expectedAccount1.getStatus());
        assertEquals(AccountStatus.BLOCKED, expectedAccount2.getStatus());
    }

    @Test
    void testFindAccountsWhereProductIdAndStatusIs() {
        // TODO выяснить как делать тест на метод который подключается к дргуим табличкам
    }

    @Test
    void findAccountsByClientUuid() {
        // given
        // when
        // then
    }

    @Test
    void findAccountByClientUuidAndType() {
        // given
        // when
        // then
    }

    @Test
    void findAllNotDeleted_success() {
        // given
        Account account1 = new Account();
        entityManager.persist(account1);

        Account account2 = new Account();
        entityManager.persist(account2);

        List<Account> expected = List.of(account1, account2);

        // when
        List<Account> actual = accountRepository.findAllNotDeleted();

        // then
        flushAndClearEntityManager();
        Account expectedAccount1 = entityManager.find(Account.class, account1.getUuid());
        Account expectedAccount2 = entityManager.find(Account.class, account2.getUuid());

        assertEquals(expected.size(), actual.size());
        assertFalse(expectedAccount1.isDeleted());
        assertFalse(expectedAccount2.isDeleted());
    }

    @Test
    void findAllNotDeleted_returnsEmptyList_success() {
        // given
        Account account1 = new Account();
        account1.setDeleted(true);
        entityManager.persist(account1);

        Account account2 = new Account();
        account2.setDeleted(true);
        entityManager.persist(account2);

        // when
        List<Account> actual = accountRepository.findAllNotDeleted();

        //then
        flushAndClearEntityManager();

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAllDeleted_success() {
        // given
        Account account1 = new Account();
        account1.setDeleted(true);
        entityManager.persist(account1);

        Account account2 = new Account();
        account2.setDeleted(true);
        entityManager.persist(account2);

        List<Account> expected = List.of(account1, account2);

        // when
        List<Account> actual = accountRepository.findAllDeleted();

        // then
        flushAndClearEntityManager();
        Account expectedAccount1 = entityManager.find(Account.class, account1.getUuid());
        Account expectedAccount2 = entityManager.find(Account.class, account2.getUuid());

        assertEquals(expected.size(), actual.size());
        assertTrue(expectedAccount1.isDeleted());
        assertTrue(expectedAccount2.isDeleted());
    }

    @Test
    void findAllDeleted_returnsEmptyList_success() {
        // given
        Account account1 = new Account();
        entityManager.persist(account1);

        Account account2 = new Account();
        entityManager.persist(account2);

        // when
        List<Account> actual = accountRepository.findAllDeleted();

        // then
        flushAndClearEntityManager();

        assertTrue(actual.isEmpty());
    }

    @Test
    void findAccountsWhereProductTypeIsAndProductStatusIs() {
        // TODO выяснить как делать тест на метод который подключается к дргуим табличкам
    }
}
