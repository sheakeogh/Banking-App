package com.bank.backend.controller;

import com.bank.backend.model.*;
import com.bank.backend.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class TransactionControllerTests {

    @Mock
    private HttpServletRequest request;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewTransactionSuccess() {
        TransactionRequest transactionRequest = createTransactionRequest();
        Transaction transaction = createUser().getAccountList().get(0).getTransactionList().get(0);

        Mockito.when(transactionService.createTransaction(Mockito.any(TransactionRequest.class), Mockito.any(HttpServletRequest.class))).thenReturn(transaction);

        ResponseEntity<?> response = transactionController.createNewTransaction(transactionRequest, request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(transaction, response.getBody());

        Mockito.verify(transactionService, Mockito.times(1)).createTransaction(Mockito.any(TransactionRequest.class), Mockito.any(HttpServletRequest.class));
    }

    @Test
    public void testCreateNewTransactionFail() {
        TransactionRequest transactionRequest = createTransactionRequest();

        Mockito.when(transactionService.createTransaction(Mockito.any(TransactionRequest.class), Mockito.any(HttpServletRequest.class))).thenReturn(null);

        ResponseEntity<?> response = transactionController.createNewTransaction(transactionRequest, request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(transactionService, Mockito.times(1)).createTransaction(Mockito.any(TransactionRequest.class), Mockito.any(HttpServletRequest.class));
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
