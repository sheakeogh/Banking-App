package com.bank.backend.service.impl;

import com.bank.backend.model.*;
import com.bank.backend.repository.AccountRepository;
import com.bank.backend.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class AccountServiceImplTests {

    @Mock
    private Random random;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateAccountSuccess() {
        AccountRequest accountRequest = createAccountRequest();
        User user = createUser();
        Account account = user.getAccountList().get(0);

        Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        Mockito.when(accountRepository.existsByAccountNumber(Mockito.anyString())).thenReturn(true, false);
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);

        Account accountResponse = accountService.createAccount(accountRequest);

        Assertions.assertNotNull(accountResponse);
        Assertions.assertEquals(account.getId(), accountResponse.getId());
        Assertions.assertEquals(account.getAccountNumber(), accountResponse.getAccountNumber());
        Assertions.assertEquals(account.getBalance(), accountResponse.getBalance());
        Assertions.assertEquals(account.getAccountType(), accountResponse.getAccountType());

        Mockito.verify(userRepository, Mockito.times(2)).findById(Mockito.any(Long.class));
        Mockito.verify(accountRepository, Mockito.times(2)).existsByAccountNumber(Mockito.anyString());
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
    }

    @Test
    public void testCreateAccountNoUser() {
        AccountRequest accountRequest = createAccountRequest();

        Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Account accountResponse = accountService.createAccount(accountRequest);

        Assertions.assertNull(accountResponse);

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    public void testCreateAccountBadRequest() {
        Account accountResponse = accountService.createAccount(null);

        Assertions.assertNull(accountResponse);
    }

    @Test
    public void testGetAccountByIdSuccess() {
        Account account = createUser().getAccountList().get(0);

        Mockito.when(accountRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(account));

        Account accountResponse = accountService.getAccountById(1L);

        Assertions.assertNotNull(accountResponse);
        Assertions.assertEquals(account.getId(), accountResponse.getId());
        Assertions.assertEquals(account.getAccountNumber(), accountResponse.getAccountNumber());
        Assertions.assertEquals(account.getBalance(), accountResponse.getBalance());
        Assertions.assertEquals(account.getAccountType(), accountResponse.getAccountType());

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    public void testGetAccountByIdFail() {
        Mockito.when(accountRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Account accountResponse = accountService.getAccountById(1L);

        Assertions.assertNull(accountResponse);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    public void testGetAllAccountsSuccess() {
        Account account = createUser().getAccountList().get(0);

        Mockito.when(accountRepository.findAll()).thenReturn(List.of(account));

        List<Account> accountResponse = accountService.getAllAccounts();

        Assertions.assertNotNull(accountResponse);
        Assertions.assertEquals(account.getId(), accountResponse.get(0).getId());
        Assertions.assertEquals(account.getAccountNumber(), accountResponse.get(0).getAccountNumber());
        Assertions.assertEquals(account.getBalance(), accountResponse.get(0).getBalance());
        Assertions.assertEquals(account.getAccountType(), accountResponse.get(0).getAccountType());

        Mockito.verify(accountRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testGetAllAccountsFail() {
        Mockito.when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        List<Account> accountResponse = accountService.getAllAccounts();

        Assertions.assertNotNull(accountResponse);
        Assertions.assertTrue(accountResponse.isEmpty());

        Mockito.verify(accountRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testDeleteByIdSuccess() {
        Mockito.when(accountRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.doNothing().when(accountRepository).deleteById(Mockito.any(Long.class));

        boolean deleteResponse = accountService.deleteAccountById(1L);

        Assertions.assertTrue(deleteResponse);

        Mockito.verify(accountRepository, Mockito.times(1)).existsById(Mockito.any(Long.class));
        Mockito.verify(accountRepository, Mockito.times(1)).deleteById(Mockito.any(Long.class));
    }

    @Test
    public void testDeleteByIdFail() {
        Mockito.when(accountRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        boolean deleteResponse = accountService.deleteAccountById(1L);

        Assertions.assertFalse(deleteResponse);

        Mockito.verify(accountRepository, Mockito.times(1)).existsById(Mockito.any(Long.class));
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Name");
        user.setLastName("Name");
        user.setEmail("mail@mail.com");
        user.setPhoneNumber("123456");
        user.setUsername("Username");
        user.setPassword("Password");
        user.setUserRole(UserRole.USER);

        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456");
        account.setBalance(0.0);
        account.setAccountType(AccountType.CURRENT);
        account.setUser(user);

        user.setAccountList(List.of(account));

        return user;
    }

    private AccountRequest createAccountRequest() {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserId(1L);
        accountRequest.setAccountType(AccountType.CURRENT);

        return accountRequest;
    }

}
