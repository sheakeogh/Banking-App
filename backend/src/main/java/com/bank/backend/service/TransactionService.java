package com.bank.backend.service;

import com.bank.backend.model.Transaction;
import com.bank.backend.model.TransactionRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface TransactionService {

    Transaction createTransaction(TransactionRequest transactionRequest, HttpServletRequest request);

}
