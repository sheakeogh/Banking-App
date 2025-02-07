package com.bank.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AdminEndpoints {

    GET_ALL_USERS("/api/users/getAllUsers/**"),
    GET_ALL_ACCOUNTS("/api/accounts/getAllAccounts/**"),
    GET_USER_BY_ID("/api/users/getUser/**"),
    UPDATE_USER_BY_ID("/api/users/updateUser/**"),
    DELETE_USER_BY_ID("/api/users/deleteUser/**");

    private final String endpoint;

}
