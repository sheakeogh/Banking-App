package com.bank.backend.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TransactionType {

    WITHDRAWAL("withdrawal"),
    LODGEMENT("lodgement");

    private final String transactionType;

}
