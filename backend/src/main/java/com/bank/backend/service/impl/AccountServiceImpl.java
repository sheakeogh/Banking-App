package com.bank.backend.service.impl;

import com.bank.backend.model.Account;
import com.bank.backend.model.AccountRequest;
import com.bank.backend.repository.AccountRepository;
import com.bank.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bank.backend.service.AccountService;
import java.util.List;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Account createAccount(AccountRequest accountRequest) {
        if(isValidAccountRequest(accountRequest)) {
            if (userRepository.findById(accountRequest.getUserId()).isPresent()) {
                Account account = new Account();
                account.setAccountNumber(createAccountNumber());
                account.setBalance(0.0);
                account.setAccountType(accountRequest.getAccountType());
                account.setUser(userRepository.findById(accountRequest.getUserId()).get());

                return accountRepository.save(account);
            }
        }

        return null;
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public boolean deleteAccountById(Long id) {
       if (accountRepository.existsById(id)) {
           accountRepository.deleteById(id);
           return true;
       }

       return false;
    }

    private String createAccountNumber() {
        String accountNumber = String.format("%06d", new Random().nextInt(1000000));
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            return createAccountNumber();
        }
        else {
            return accountNumber;
        }
    }

    private boolean isValidAccountRequest(AccountRequest accountRequest) {
        return accountRequest != null &&
                accountRequest.getAccountType() != null &&
                accountRequest.getUserId() != null;
    }

}
