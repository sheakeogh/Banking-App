package com.bank.backend.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionRequest {

    private Long accountId;
    private double amount;
    private String description;
    @Enumerated(value = EnumType.STRING)
    private TransactionType transactionType;

}
