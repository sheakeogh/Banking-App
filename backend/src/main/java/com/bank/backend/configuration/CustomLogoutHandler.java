package com.bank.backend.configuration;

import com.bank.backend.model.Token;
import com.bank.backend.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Optional;

@Configuration
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);
        Optional<Token> storedToken = tokenRepository.findByAccessToken(token);

        if(storedToken.isPresent()) {
            storedToken.get().setLoggedOut(true);
            tokenRepository.save(storedToken.get());
        }
    }
}