package com.bank.backend.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {

    private Long userId;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

}
