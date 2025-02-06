package com.bank.backend.service.impl;

import com.bank.backend.model.*;
import com.bank.backend.repository.TokenRepository;
import com.bank.backend.repository.UserRepository;
import com.bank.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateNewUserSuccess() {
        User user = createUser();
        UserRequest userRequest = createUserRequest();

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(jwtService.generateAccessToken(Mockito.any(User.class))).thenReturn("accessToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any(User.class))).thenReturn("refreshToken");
        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(user.getTokenList().get(0));

        AuthenticationResponse authenticationResponse = userService.createNewUser(userRequest);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals("accessToken", authenticationResponse.getAccessToken());
        Assertions.assertEquals("refreshToken", authenticationResponse.getRefreshToken());
        Assertions.assertEquals("User Registration Was Successful.", authenticationResponse.getMessage());

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateAccessToken(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateRefreshToken(Mockito.any(User.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void testCreateNewUserUsernameExists() {
        User user = createUser();
        UserRequest userRequest = createUserRequest();

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        AuthenticationResponse authenticationResponse = userService.createNewUser(userRequest);

        Assertions.assertNull(authenticationResponse);

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test
    public void testCreateNewUserNullUser() {
        AuthenticationResponse authenticationResponse = userService.createNewUser(null);

        Assertions.assertNull(authenticationResponse);
    }

    @Test
    public void testLoginUser() {
        User user = createUser();
        LoginRequest loginRequest = createLoginRequest();

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mockito.mock(Authentication.class));
        Mockito.when(jwtService.generateAccessToken(Mockito.any(User.class))).thenReturn("accessToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any(User.class))).thenReturn("refreshToken");
        Mockito.when(tokenRepository.findAllAccessTokensByUser(Mockito.any(Long.class))).thenReturn(user.getTokenList());
        Mockito.when(tokenRepository.saveAll(Mockito.anyList())).thenReturn(user.getTokenList());
        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(user.getTokenList().get(0));

        AuthenticationResponse authenticationResponse = userService.loginUser(loginRequest);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals("accessToken", authenticationResponse.getAccessToken());
        Assertions.assertEquals("refreshToken", authenticationResponse.getRefreshToken());
        Assertions.assertEquals("User Login Was Successful.", authenticationResponse.getMessage());


        Mockito.verify(userRepository, Mockito.times(2)).findByUsername(Mockito.anyString());
        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateAccessToken(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateRefreshToken(Mockito.any(User.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).findAllAccessTokensByUser(Mockito.any(Long.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).saveAll(Mockito.anyList());
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void testLoginUserNoTokens() {
        User user = createUser();
        LoginRequest loginRequest = createLoginRequest();

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(Mockito.mock(Authentication.class));
        Mockito.when(jwtService.generateAccessToken(Mockito.any(User.class))).thenReturn("accessToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any(User.class))).thenReturn("refreshToken");
        Mockito.when(tokenRepository.findAllAccessTokensByUser(Mockito.any(Long.class))).thenReturn(Collections.emptyList());
        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(user.getTokenList().get(0));

        AuthenticationResponse authenticationResponse = userService.loginUser(loginRequest);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals("accessToken", authenticationResponse.getAccessToken());
        Assertions.assertEquals("refreshToken", authenticationResponse.getRefreshToken());
        Assertions.assertEquals("User Login Was Successful.", authenticationResponse.getMessage());

        Mockito.verify(userRepository, Mockito.times(2)).findByUsername(Mockito.anyString());
        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateAccessToken(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateRefreshToken(Mockito.any(User.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).findAllAccessTokensByUser(Mockito.any(Long.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void testLoginUserUsernameNotExists() {
        LoginRequest loginRequest = createLoginRequest();

        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        AuthenticationResponse authenticationResponse = userService.loginUser(loginRequest);

        Assertions.assertNull(authenticationResponse);

        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test
    public void testLoginUserNullUser() {
        AuthenticationResponse authenticationResponse = userService.loginUser(null);

        Assertions.assertNull(authenticationResponse);
    }

    @Test
    public void testRefreshTokenSuccess() {
        User user = createUser();
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + user.getTokenList().get(0).getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(jwtService.isValidRefreshToken(Mockito.anyString(), Mockito.any(User.class))).thenReturn(true);
        Mockito.when(jwtService.generateAccessToken(Mockito.any(User.class))).thenReturn("newAccessToken");
        Mockito.when(jwtService.generateRefreshToken(Mockito.any(User.class))).thenReturn("newRefreshToken");
        Mockito.when(tokenRepository.findAllAccessTokensByUser(Mockito.any(Long.class))).thenReturn(user.getTokenList());
        Mockito.when(tokenRepository.saveAll(Mockito.anyList())).thenReturn(user.getTokenList());
        Mockito.when(tokenRepository.save(Mockito.any(Token.class))).thenReturn(user.getTokenList().get(0));

        AuthenticationResponse authenticationResponse = userService.refreshToken(mockRequest, mockResponse);

        Assertions.assertNotNull(authenticationResponse);
        Assertions.assertEquals("newAccessToken", authenticationResponse.getAccessToken());
        Assertions.assertEquals("newRefreshToken", authenticationResponse.getRefreshToken());
        Assertions.assertEquals("New Token Generated.", authenticationResponse.getMessage());

        Mockito.verify(mockRequest, Mockito.times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(jwtService, Mockito.times(1)).isValidRefreshToken(Mockito.anyString(), Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateAccessToken(Mockito.any(User.class));
        Mockito.verify(jwtService, Mockito.times(1)).generateRefreshToken(Mockito.any(User.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).findAllAccessTokensByUser(Mockito.any(Long.class));
        Mockito.verify(tokenRepository, Mockito.times(1)).saveAll(Mockito.anyList());
        Mockito.verify(tokenRepository, Mockito.times(1)).save(Mockito.any(Token.class));
    }

    @Test
    public void testRefreshTokenNullHeader() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletRequest mockRequestNull = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Test: Token");
        Mockito.when(mockRequestNull.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        AuthenticationResponse authenticationResponse = userService.refreshToken(mockRequest, mockResponse);
        AuthenticationResponse authenticationResponseNullHeader = userService.refreshToken(mockRequestNull, mockResponse);

        Assertions.assertNull(authenticationResponse);
        Assertions.assertNull(authenticationResponseNullHeader);

        Mockito.verify(mockRequest, Mockito.times(1)).getHeader(Mockito.any());
        Mockito.verify(mockRequestNull, Mockito.times(1)).getHeader(Mockito.any());
    }

    @Test
    public void testRefreshTokenNullUser() {
        User user = createUser();
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + user.getTokenList().get(0).getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.empty());

        AuthenticationResponse authenticationResponse = userService.refreshToken(mockRequest, mockResponse);

        Assertions.assertNull(authenticationResponse);

        Mockito.verify(mockRequest, Mockito.times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test
    public void testRefreshTokenInvalidToken() {
        User user = createUser();
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(mockRequest.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + user.getTokenList().get(0).getRefreshToken());
        Mockito.when(jwtService.extractUsername(Mockito.anyString())).thenReturn(user.getUsername());
        Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(jwtService.isValidRefreshToken(Mockito.anyString(), Mockito.any(User.class))).thenReturn(false);

        AuthenticationResponse authenticationResponse = userService.refreshToken(mockRequest, mockResponse);

        Assertions.assertNull(authenticationResponse);

        Mockito.verify(mockRequest, Mockito.times(1)).getHeader(HttpHeaders.AUTHORIZATION);
        Mockito.verify(jwtService, Mockito.times(1)).extractUsername(Mockito.anyString());
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(Mockito.anyString());
        Mockito.verify(jwtService, Mockito.times(1)).isValidRefreshToken(Mockito.anyString(), Mockito.any(User.class));
    }

    @Test
    public void testGetUserByIdSuccess() {
        User user = createUser();

        Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));

        User userResponse = userService.getUserById(1L);

        Assertions.assertNotNull(userResponse);
        Assertions.assertEquals(user.getId(), userResponse.getId());
        Assertions.assertEquals(user.getFirstName(), userResponse.getFirstName());
        Assertions.assertEquals(user.getLastName(), userResponse.getLastName());
        Assertions.assertEquals(user.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(user.getPhoneNumber(), userResponse.getPhoneNumber());
        Assertions.assertEquals(user.getUsername(), userResponse.getUsername());
        Assertions.assertEquals(user.getPassword(), userResponse.getPassword());
        Assertions.assertEquals(user.getUserRole(), userResponse.getUserRole());
        Assertions.assertTrue(userResponse.getAuthorities().contains(new SimpleGrantedAuthority(userResponse.getUserRole().name())));
        Assertions.assertTrue(userResponse.isAccountNonExpired());
        Assertions.assertTrue(userResponse.isAccountNonLocked());
        Assertions.assertTrue(userResponse.isCredentialsNonExpired());
        Assertions.assertTrue(userResponse.isEnabled());

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    public void testGetUserByIdNull() {
        Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        User userResponse = userService.getUserById(1L);

        Assertions.assertNull(userResponse);

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    public void testGetAllUsersSuccess() {
        User user = createUser();

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> userResponseList = userService.getAllUsers();
        User userResponse = userResponseList.get(0);

        Assertions.assertNotNull(userResponseList);
        Assertions.assertEquals(user.getId(), userResponse.getId());
        Assertions.assertEquals(user.getFirstName(), userResponse.getFirstName());
        Assertions.assertEquals(user.getLastName(), userResponse.getLastName());
        Assertions.assertEquals(user.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(user.getPhoneNumber(), userResponse.getPhoneNumber());
        Assertions.assertEquals(user.getUsername(), userResponse.getUsername());
        Assertions.assertEquals(user.getPassword(), userResponse.getPassword());
        Assertions.assertEquals(user.getUserRole(), userResponse.getUserRole());

        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void testUpdateUserByIdSuccess() {
        User user = createUser();
        UserRequest userRequest = createUserRequest();

        Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User userResponse = userService.updateUserById(userRequest, 1L);

        Assertions.assertNotNull(userResponse);
        Assertions.assertEquals(user.getId(), userResponse.getId());
        Assertions.assertEquals(user.getFirstName(), userResponse.getFirstName());
        Assertions.assertEquals(user.getLastName(), userResponse.getLastName());
        Assertions.assertEquals(user.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(user.getPhoneNumber(), userResponse.getPhoneNumber());
        Assertions.assertEquals(user.getUsername(), userResponse.getUsername());
        Assertions.assertEquals(user.getPassword(), userResponse.getPassword());
        Assertions.assertEquals(user.getUserRole(), userResponse.getUserRole());

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testUpdateUserByIdNull() {
        UserRequest userRequest = createUserRequest();

        Mockito.when(userRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

        User userResponse = userService.updateUserById(userRequest, 1L);

        Assertions.assertNull(userResponse);

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any(Long.class));
    }

    @Test
    public void testDeleteUserByIdSuccess() {
        Mockito.when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(true);
        Mockito.doNothing().when(userRepository).deleteById(Mockito.any(Long.class));

        boolean response = userService.deleteUserById(1L);

        Assertions.assertTrue(response);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(Mockito.any(Long.class));
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(Mockito.any(Long.class));
    }

    @Test
    public void testDeleteUserByIdNull() {
        Mockito.when(userRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        boolean response = userService.deleteUserById(1L);

        Assertions.assertFalse(response);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(Mockito.any(Long.class));
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Name");
        user.setLastName("Name");
        user.setEmail("mail@mail.com");
        user.setPhoneNumber("123456");
        user.setUsername("Username");
        user.setPassword("Password");
        user.setUserRole(UserRole.USER);

        Token token = new Token();
        token.setId(1L);
        token.setAccessToken("accessToken");
        token.setRefreshToken("refreshToken");
        token.setLoggedOut(false);
        token.setUser(user);

        user.setTokenList(List.of(token));

        return user;
    }

    private UserRequest createUserRequest() {
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("Name");
        userRequest.setLastName("Name");
        userRequest.setEmail("mail@mail.com");
        userRequest.setPhoneNumber("123456");
        userRequest.setUsername("Username");
        userRequest.setPassword("Password");
        userRequest.setUserRole(UserRole.USER);

        return userRequest;
    }

    private LoginRequest createLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Username");
        loginRequest.setPassword("Password");

        return loginRequest;
    }

}