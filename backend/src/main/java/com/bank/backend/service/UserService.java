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
    AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response);
    User getUserById(Long id);
    List<User> getAllUsers();
    User updateUserById(UserRequest userRequest, Long id);
    boolean deleteUserById(Long id);

}
