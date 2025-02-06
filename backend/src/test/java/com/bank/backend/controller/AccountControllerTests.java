package com.bank.backend.controller;

import com.bank.backend.model.*;
import com.bank.backend.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class AccountControllerTests {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewAccountSuccess() {
        AccountRequest accountRequest = createAccountRequest();
        Account account = createUser().getAccountList().get(0);

        Mockito.when(accountService.createAccount(Mockito.any(AccountRequest.class))).thenReturn(account);

        ResponseEntity<?> response = accountController.createNewAccount(accountRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(account, response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).createAccount(Mockito.any(AccountRequest.class));
    }

    @Test
    public void testCreateNewAccountFail() {
        AccountRequest accountRequest = createAccountRequest();

        Mockito.when(accountService.createAccount(Mockito.any(AccountRequest.class))).thenReturn(null);

        ResponseEntity<?> response = accountController.createNewAccount(accountRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(accountService, Mockito.times(1)).createAccount(Mockito.any(AccountRequest.class));
    }

    @Test
    public void testGetAccountByIdSuccess() {
        Account account = createUser().getAccountList().get(0);

        Mockito.when(accountService.getAccountById(Mockito.any(Long.class))).thenReturn(account);

        ResponseEntity<?> response = accountController.getAccountById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(account, response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).getAccountById(Mockito.any(Long.class));
    }

    @Test
    public void testGetAccountByIdFail() {
        Mockito.when(accountService.getAccountById(Mockito.any(Long.class))).thenReturn(null);

        ResponseEntity<?> response = accountController.getAccountById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(accountService, Mockito.times(1)).getAccountById(Mockito.any(Long.class));
    }

    @Test
    public void testGetAllAccountsSuccess() {
        List<Account> accountList = createUser().getAccountList();

        Mockito.when(accountService.getAllAccounts()).thenReturn(accountList);

        ResponseEntity<?> response = accountController.getAllAccounts();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(accountList, response.getBody());

        Mockito.verify(accountService, Mockito.times(1)).getAllAccounts();
    }

    @Test
    public void testGetAllAccountsFail() {
        Mockito.when(accountService.getAllAccounts()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = accountController.getAllAccounts();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(accountService, Mockito.times(1)).getAllAccounts();
    }

    @Test
    public void testDeleteAccountByIdSuccess() {
        Mockito.when(accountService.deleteAccountById(Mockito.any(Long.class))).thenReturn(true);

        ResponseEntity<?> response = accountController.deleteAccountById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Mockito.verify(accountService, Mockito.times(1)).deleteAccountById(Mockito.any(Long.class));
    }

    @Test
    public void testDeleteAccountByIdFail() {
        Mockito.when(accountService.deleteAccountById(Mockito.any(Long.class))).thenReturn(false);

        ResponseEntity<?> response = accountController.deleteAccountById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(accountService, Mockito.times(1)).deleteAccountById(Mockito.any(Long.class));
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
        accountRequest.setAccountType(AccountType.CURRENT);
        accountRequest.setUserId(1L);

        return accountRequest;
    }
}
