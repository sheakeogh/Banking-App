package com.bank.backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;
    private String message;

}
