package com.bank.backend.service;

import com.bank.backend.model.AuthenticationResponse;
import com.bank.backend.model.LoginRequest;
import com.bank.backend.model.User;
import com.bank.backend.model.UserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

    AuthenticationResponse createNewUser(UserRequest userRequest);
    AuthenticationResponse loginUser(LoginRequest userRequest);
    AuthenticationResponse refreshToken(HttpServletRequest request);
    User getLoggedInUser(HttpServletRequest request);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateLoggedInUser(HttpServletRequest request, UserRequest userRequest);
    User updateUserById(UserRequest userRequest, Long id);
    boolean deleteLoggedInUser(HttpServletRequest request);
    boolean deleteUserById(Long id);

}
