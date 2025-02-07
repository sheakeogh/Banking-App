package com.bank.backend.service.impl;

import com.bank.backend.model.Account;
import com.bank.backend.model.Transaction;
import com.bank.backend.model.TransactionRequest;
import com.bank.backend.repository.AccountRepository;
import com.bank.backend.repository.TransactionRepository;
import com.bank.backend.service.AccountService;
import com.bank.backend.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public Transaction createTransaction(TransactionRequest transactionRequest, HttpServletRequest request) {
        if (isValidTransactionRequest(transactionRequest)) {
            Account account = accountRepository.findById(transactionRequest.getAccountId()).orElse(null);
            if (account != null && isUserAllowed(transactionRequest.getAccountId(), request)
                    && (account.getBalance() - transactionRequest.getAmount() >= 0.0)) {
                account.setBalance(account.getBalance() - transactionRequest.getAmount());
                accountRepository.save(account);

                Transaction transaction = new Transaction();
                transaction.setAmount(transactionRequest.getAmount());
                transaction.setDescription(transactionRequest.getDescription());
                transaction.setTransactionType(transactionRequest.getTransactionType());
                transaction.setAccount(account);

                return transactionRepository.save(transaction);
            }
        }

        return null;
    }

    private boolean isValidTransactionRequest(TransactionRequest transactionRequest) {
        return transactionRequest != null &&
                transactionRequest.getAccountId() != null &&
                transactionRequest.getTransactionType() != null &&
                transactionRequest.getAmount() != 0.0 &&
                transactionRequest.getDescription() != null;
    }

    private boolean isUserAllowed(Long id, HttpServletRequest request) {
        return accountService.getLoggedInAccounts(request).stream()
                .anyMatch(account -> Objects.equals(account.getId(), id));
    }

}
