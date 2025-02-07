package com.bank.backend.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {

    ADMIN("admin"),
    USER("user");

    private final String userRole;

}
