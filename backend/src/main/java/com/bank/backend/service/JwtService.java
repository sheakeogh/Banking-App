package com.bank.backend.service;

import com.bank.backend.model.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);
    boolean isValid(String token, UserDetails user);
    boolean isValidRefreshToken(String token, User user);
    <T> T extractClaim(String token, Function<Claims, T> resolver);
    String generateAccessToken(User user);
    String generateRefreshToken(User user);

}