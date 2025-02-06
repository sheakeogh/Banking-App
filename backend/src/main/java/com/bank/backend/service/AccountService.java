package com.bank.backend.service;

import com.bank.backend.model.Account;
import com.bank.backend.model.AccountRequest;

import java.util.List;

public interface AccountService {

    Account createAccount(AccountRequest accountRequest);
    Account getAccountById(Long id);
    List<Account> getAllAccounts();
    boolean deleteAccountById(Long id);

}
