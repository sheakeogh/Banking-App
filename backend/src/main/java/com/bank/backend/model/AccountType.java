package com.bank.backend.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AccountType {

    CURRENT("current"),
    SAVINGS("savings");

    private final String accountType;

}
