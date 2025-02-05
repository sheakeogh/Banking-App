package com.bank.backend.controller;

import com.bank.backend.exception.InvalidRequestException;
import com.bank.backend.model.AuthenticationResponse;
import com.bank.backend.model.LoginRequest;
import com.bank.backend.model.User;
import com.bank.backend.model.UserRequest;
import com.bank.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        AuthenticationResponse authenticationResponse = userService.createNewUser(userRequest);
        if (authenticationResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Error with Data Passed. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
    }

    @PostMapping("/auth/loginUser")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authenticationResponse = userService.loginUser(loginRequest);
        if (authenticationResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Error with Data Passed. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }

    @PostMapping("/auth/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = userService.refreshToken(request, response);
        if (authenticationResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Error with Data Passed. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No User Found With ID: " + id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<?> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        if (userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No Users Found."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUserById(@RequestBody UserRequest userRequest, @PathVariable Long id) {
        User user = userService.updateUserById(userRequest, id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No User Found With ID: " + id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        if (userService.deleteUserById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User Deleted SuccessFully.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No User Found With ID: " + id));
    }

}