package com.bank.backend.service.impl;

import com.bank.backend.model.*;
import com.bank.backend.repository.TokenRepository;
import com.bank.backend.repository.UserRepository;
import com.bank.backend.service.UserService;
import com.bank.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse createNewUser(UserRequest userRequest) {
        if (!isValidUserRequest(userRequest) || userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            return null;
        }

        User user = saveUser(userRequest, new User());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken,"User Registration Was Successful.");
    }

    @Override
    public AuthenticationResponse loginUser(LoginRequest loginRequest) {
        if (!isValidLoginRequest(loginRequest) || userRepository.findByUsername(loginRequest.getUsername()).isEmpty()) {
            return null;
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        User user = userRepository.findByUsername(loginRequest.getUsername()).get();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User Login Was Successful.");

    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent() && jwtService.isValidRefreshToken(token, user.get())) {
            String accessToken = jwtService.generateAccessToken(user.get());
            String refreshToken = jwtService.generateRefreshToken(user.get());
            revokeAllTokenByUser(user.get());
            saveUserToken(accessToken, refreshToken, user.get());

            return new AuthenticationResponse(accessToken, refreshToken, "New Token Generated.");
        }

        return null;
    }

    @Override
    public User getLoggedInUser(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateLoggedInUser(HttpServletRequest request, UserRequest userRequest) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        Optional<User> user = userRepository.findByUsername(username);
        return user.map(value -> saveUser(userRequest, value)).orElse(null);
    }

    @Override
    public User updateUserById(UserRequest userRequest, Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> saveUser(userRequest, value)).orElse(null);
    }

    @Override
    public boolean deleteLoggedInUser(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            userRepository.deleteById(user.get().getId());
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private User saveUser(UserRequest userRequest, User user) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setUserRole(userRequest.getUserRole());

        return userRepository.save(user);
    }

    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> t.setLoggedOut(true));
        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);

        tokenRepository.save(token);
    }

    private boolean isValidUserRequest(UserRequest userRequest) {
        return userRequest != null &&
                userRequest.getFirstName() != null &&
                userRequest.getLastName() != null &&
                userRequest.getEmail() != null &&
                userRequest.getPhoneNumber() != null &&
                userRequest.getUsername() != null &&
                userRequest.getPassword() != null &&
                userRequest.getUserRole() != null;
    }

    private boolean isValidLoginRequest(LoginRequest loginRequest) {
        return loginRequest != null &&
                loginRequest.getUsername() != null &&
                loginRequest.getPassword() != null;
    }

}