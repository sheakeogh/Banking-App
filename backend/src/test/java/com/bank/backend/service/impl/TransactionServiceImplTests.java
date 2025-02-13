package com.bank.backend.service.impl;

import com.bank.backend.model.*;
import com.bank.backend.repository.AccountRepository;
import com.bank.backend.repository.TransactionRepository;
import com.bank.backend.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
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

public class TransactionServiceImplTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTransactionSuccess() {
        TransactionRequest transactionRequest = createTransactionRequest();
        Account account = createUser().getAccountList().get(0);
        Transaction transaction = account.getTransactionList().get(0);

        Mockito.when(accountRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(account));
        Mockito.when(accountService.getLoggedInAccounts(Mockito.any(HttpServletRequest.class))).thenReturn(List.of(account));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

        Transaction transactionResponse = transactionService.createTransaction(transactionRequest, request);

        Assertions.assertNotNull(transactionResponse);
        Assertions.assertEquals(transaction.getId(), transactionResponse.getId());
        Assertions.assertEquals(transaction.getTransactionType(), transactionResponse.getTransactionType());
        Assertions.assertEquals(transaction.getAccount(), transactionResponse.getAccount());
        Assertions.assertEquals(transaction.getAmount(), transactionResponse.getAmount());
        Assertions.assertEquals(transaction.getDescription(), transactionResponse.getDescription());

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verify(accountService, Mockito.times(1)).getLoggedInAccounts(Mockito.any(HttpServletRequest.class));
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    public void testCreateTransactionLodgement() {
        TransactionRequest transactionRequest = createTransactionRequest();
        Account account = createUser().getAccountList().get(0);
        Transaction transaction = account.getTransactionList().get(0);
        transactionRequest.setTransactionType(TransactionType.LODGEMENT);

        Mockito.when(accountRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(account));
        Mockito.when(accountService.getLoggedInAccounts(Mockito.any(HttpServletRequest.class))).thenReturn(List.of(account));
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);

        Transaction transactionResponse = transactionService.createTransaction(transactionRequest, request);

        Assertions.assertNotNull(transactionResponse);
        Assertions.assertEquals(transaction.getId(), transactionResponse.getId());
        Assertions.assertEquals(transaction.getTransactionType(), transactionResponse.getTransactionType());
        Assertions.assertEquals(transaction.getAccount(), transactionResponse.getAccount());
        Assertions.assertEquals(transaction.getAmount(), transactionResponse.getAmount());
        Assertions.assertEquals(transaction.getDescription(), transactionResponse.getDescription());

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verify(accountService, Mockito.times(1)).getLoggedInAccounts(Mockito.any(HttpServletRequest.class));
        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
    }

    @Test
    public void testCreateTransactionInvalidRequest() {
        Transaction transactionResponse = transactionService.createTransaction(null, request);

        Assertions.assertNull(transactionResponse);
    }

    @Test
    public void testCreateTransactionNullAccount() {
        TransactionRequest transactionRequest = createTransactionRequest();

        Mockito.when(accountRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        Transaction transactionResponse = transactionService.createTransaction(transactionRequest, request);

        Assertions.assertNull(transactionResponse);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    public void testCreateTransactionUserNotAllowed() {
        TransactionRequest transactionRequest = createTransactionRequest();
        Account account = createUser().getAccountList().get(0);

        Mockito.when(accountRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(account));
        Mockito.when(accountService.getLoggedInAccounts(Mockito.any(HttpServletRequest.class))).thenReturn(Collections.emptyList());

        Transaction transactionResponse = transactionService.createTransaction(transactionRequest, request);

        Assertions.assertNull(transactionResponse);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verify(accountService, Mockito.times(1)).getLoggedInAccounts(Mockito.any(HttpServletRequest.class));
    }

    @Test
    public void testCreateTransactionUserNoMoney() {
        TransactionRequest transactionRequest = createTransactionRequest();
        Account account = createUser().getAccountList().get(0);
        account.setBalance(5.00);

        Mockito.when(accountRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(account));
        Mockito.when(accountService.getLoggedInAccounts(Mockito.any(HttpServletRequest.class))).thenReturn(List.of(account));

        Transaction transactionResponse = transactionService.createTransaction(transactionRequest, request);

        Assertions.assertNull(transactionResponse);

        Mockito.verify(accountRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verify(accountService, Mockito.times(1)).getLoggedInAccounts(Mockito.any(HttpServletRequest.class));
    }

    private TransactionRequest createTransactionRequest() {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(10.00);
        transactionRequest.setDescription("Payment");
        transactionRequest.setTransactionType(TransactionType.WITHDRAWAL);
        transactionRequest.setAccountId(1L);

        return transactionRequest;
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

        Token token = new Token();
        token.setId(1L);
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setLoggedOut(false);
        token.setUser(user);

        Account account = new Account();
        account.setId(1L);
        account.setAccountNumber("123456");
        account.setBalance(100.00);
        account.setAccountType(AccountType.CURRENT);
        account.setUser(user);

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(10.00);
        transaction.setDescription("Payment");
        transaction.setTransactionType(TransactionType.WITHDRAWAL);
        transaction.setAccount(account);

        account.setTransactionList(List.of(transaction));
        user.setTokenList(List.of(token));
        user.setAccountList(List.of(account));

        return user;
    }

}
