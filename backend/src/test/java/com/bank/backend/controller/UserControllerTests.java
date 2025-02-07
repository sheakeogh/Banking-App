package com.bank.backend.controller;

import com.bank.backend.model.*;
import com.bank.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserSuccess() {
        UserRequest userRequest = createUserRequest();
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("accessToken", "refreshToken", "User Registration Was Successful.");

        Mockito.when(userService.createNewUser(Mockito.any(UserRequest.class))).thenReturn(authenticationResponse);

        ResponseEntity<?> response = userController.createUser(userRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(authenticationResponse, response.getBody());

        Mockito.verify(userService, Mockito.times(1)).createNewUser(Mockito.any(UserRequest.class));
    }

    @Test
    public void testCreateUserFail() {
        UserRequest userRequest = createUserRequest();

        Mockito.when(userService.createNewUser(Mockito.any(UserRequest.class))).thenReturn(null);

        ResponseEntity<?> response = userController.createUser(userRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).createNewUser(Mockito.any(UserRequest.class));
    }

    @Test
    public void testLoginUserSuccess() {
        LoginRequest loginRequest = createLoginRequest();
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("accessToken", "refreshToken", "User Login Was Successful.");

        Mockito.when(userService.loginUser(Mockito.any(LoginRequest.class))).thenReturn(authenticationResponse);

        ResponseEntity<?> response = userController.loginUser(loginRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(authenticationResponse, response.getBody());

        Mockito.verify(userService, Mockito.times(1)).loginUser(Mockito.any(LoginRequest.class));
    }

    @Test
    public void testLoginUserFail() {
        LoginRequest loginRequest = createLoginRequest();

        Mockito.when(userService.loginUser(Mockito.any(LoginRequest.class))).thenReturn(null);

        ResponseEntity<?> response = userController.loginUser(loginRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).loginUser(Mockito.any(LoginRequest.class));
    }

    @Test
    public void testRefreshTokenSuccess() {
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("accessToken", "refreshToken", "User Login Was Successful.");

        Mockito.when(userService.refreshToken(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class))).thenReturn(authenticationResponse);

        ResponseEntity<?> response = userController.refreshToken(request, mockResponse);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(authenticationResponse, response.getBody());

        Mockito.verify(userService, Mockito.times(1)).refreshToken(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void testRefreshTokenFail() {
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

        Mockito.when(userService.refreshToken(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class))).thenReturn(null);

        ResponseEntity<?> response = userController.refreshToken(request, mockResponse);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).refreshToken(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }

    @Test
    public void testGetLoggedInUserSuccess() {
        User user = createUser();

        Mockito.when(userService.getLoggedInUser(Mockito.any(HttpServletRequest.class))).thenReturn(user);

        ResponseEntity<?> response = userController.getLoggedInUser(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());

        Mockito.verify(userService, Mockito.times(1)).getLoggedInUser(Mockito.any(HttpServletRequest.class));
    }

    @Test
    public void testGetLoggedInUserFail() {
        Mockito.when(userService.getLoggedInUser(Mockito.any(HttpServletRequest.class))).thenReturn(null);

        ResponseEntity<?> response = userController.getLoggedInUser(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).getLoggedInUser(Mockito.any(HttpServletRequest.class));
    }

    @Test
    public void testGetUserByIdSuccess() {
        User user = createUser();

        Mockito.when(userService.getUserById(Mockito.any(Long.class))).thenReturn(user);

        ResponseEntity<?> response = userController.getUserById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());

        Mockito.verify(userService, Mockito.times(1)).getUserById(Mockito.any(Long.class));
    }

    @Test
    public void testGetUserByIdFail() {
        Mockito.when(userService.getUserById(Mockito.any(Long.class))).thenReturn(null);

        ResponseEntity<?> response = userController.getUserById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).getUserById(Mockito.any(Long.class));
    }

    @Test
    public void testGetAllUsersSuccess() {
        User user = createUser();

        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user));

        ResponseEntity<?> response = userController.getAllUsers();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(List.of(user), response.getBody());

        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
    }

    @Test
    public void testGetAllUsersFail() {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = userController.getAllUsers();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).getAllUsers();
    }

    @Test
    public void testUpdateLoggedInUserSuccess() {
        UserRequest userRequest = createUserRequest();
        User user = createUser();

        Mockito.when(userService.updateLoggedInUser(Mockito.any(HttpServletRequest.class), Mockito.any(UserRequest.class))).thenReturn(user);

        ResponseEntity<?> response = userController.updateLoggedInUser(request, userRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());

        Mockito.verify(userService, Mockito.times(1)).updateLoggedInUser(Mockito.any(HttpServletRequest.class), Mockito.any(UserRequest.class));
    }

    @Test
    public void testUpdateLoggedInUserFail() {
        UserRequest userRequest = createUserRequest();

        Mockito.when(userService.updateLoggedInUser(Mockito.any(HttpServletRequest.class), Mockito.any(UserRequest.class))).thenReturn(null);

        ResponseEntity<?> response = userController.updateLoggedInUser(request, userRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).updateLoggedInUser(Mockito.any(HttpServletRequest.class), Mockito.any(UserRequest.class));
    }

    @Test
    public void testUpdateUserByIdSuccess() {
        User user = createUser();
        UserRequest userRequest = createUserRequest();

        Mockito.when(userService.updateUserById(Mockito.any(UserRequest.class), Mockito.any(Long.class))).thenReturn(user);

        ResponseEntity<?> response = userController.updateUserById(userRequest, 1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());

        Mockito.verify(userService, Mockito.times(1)).updateUserById(Mockito.any(UserRequest.class), Mockito.any(Long.class));
    }

    @Test
    public void testUpdateUserByIdFail() {
        UserRequest userRequest = createUserRequest();
        Mockito.when(userService.updateUserById(Mockito.any(UserRequest.class), Mockito.any(Long.class))).thenReturn(null);

        ResponseEntity<?> response = userController.updateUserById(userRequest, 1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).updateUserById(Mockito.any(UserRequest.class), Mockito.any(Long.class));
    }

    @Test
    public void testDeleteLoggedInUserSuccess() {
        Mockito.when(userService.deleteLoggedInUser(Mockito.any(HttpServletRequest.class))).thenReturn(true);

        ResponseEntity<?> response = userController.deleteLoggedInUser(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).deleteLoggedInUser(Mockito.any(HttpServletRequest.class));
    }

    @Test
    public void testDeleteLoggedInUserFail() {
        Mockito.when(userService.deleteLoggedInUser(Mockito.any(HttpServletRequest.class))).thenReturn(false);

        ResponseEntity<?> response = userController.deleteLoggedInUser(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).deleteLoggedInUser(Mockito.any(HttpServletRequest.class));
    }

    @Test
    public void testDeleteUserByIdSuccess() {
        Mockito.when(userService.deleteUserById(Mockito.any(Long.class))).thenReturn(true);

        ResponseEntity<?> response = userController.deleteUserById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).deleteUserById(Mockito.any(Long.class));
    }

    @Test
    public void testDeleteUserByIdFail() {
        Mockito.when(userService.deleteUserById(Mockito.any(Long.class))).thenReturn(false);

        ResponseEntity<?> response = userController.deleteUserById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        Mockito.verify(userService, Mockito.times(1)).deleteUserById(Mockito.any(Long.class));
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

}