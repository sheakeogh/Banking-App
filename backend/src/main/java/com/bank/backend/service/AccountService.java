package com.bank.backend.service;

import com.bank.backend.model.Account;
import com.bank.backend.model.AccountRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AccountService {

    Account createAccount(AccountRequest accountRequest);
    List<Account> getLoggedInAccounts(HttpServletRequest request);
    Account getAccountById(Long id);
    List<Account> getAllAccounts();
    boolean deleteAccountById(Long id, HttpServletRequest request);

}
