package com.bank.backend.controller;

import com.bank.backend.exception.InvalidRequestException;
import com.bank.backend.model.AuthenticationResponse;
import com.bank.backend.model.LoginRequest;
import com.bank.backend.model.User;
import com.bank.backend.model.UserRequest;
import com.bank.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/auth/createUser")
    @Operation(summary = "Create User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create User", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        AuthenticationResponse authenticationResponse = userService.createNewUser(userRequest);
        if (authenticationResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Error with Data Passed. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
    }

    @PostMapping("/auth/loginUser")
    @Operation(summary = "Login User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login User", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authenticationResponse = userService.loginUser(loginRequest);
        if (authenticationResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Error with Data Passed. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }

    @PostMapping("/auth/refreshToken")
    @Operation(summary = "Refresh Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Refresh Token", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        AuthenticationResponse authenticationResponse = userService.refreshToken(request);
        if (authenticationResponse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Error with Data Passed. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }

    @GetMapping("/getLoggedInUser")
    @Operation(summary = "Get Logged In User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Logged In User", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> getLoggedInUser(HttpServletRequest request) {
        User user = userService.getLoggedInUser(request);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No User Found."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/getUser/{id}")
    @Operation(summary = "Get User By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get User By Id", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No User Found With ID: " + id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/getAllUsers")
    @Operation(summary = "Get All Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Users", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        if (userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No Users Found."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    @GetMapping("/updateLoggedInUser")
    @Operation(summary = "Update Logged In User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Logged In User", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> updateLoggedInUser(HttpServletRequest request, @RequestBody UserRequest userRequest) {
        User user = userService.updateLoggedInUser(request, userRequest);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No User Found."));
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/updateUser/{id}")
    @Operation(summary = "Update User By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update User By Id", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> updateUserById(@RequestBody UserRequest userRequest, @PathVariable Long id) {
        User user = userService.updateUserById(userRequest, id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No User Found With ID: " + id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/deleteLoggedInUser")
    @Operation(summary = "Delete Logged In User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete Logged In User", content = {@Content(mediaType = "application/json", schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> deleteLoggedInUser(HttpServletRequest request) {
        if (userService.deleteLoggedInUser(request)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User Deleted Successfully.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Could Not Delete User."));
    }

    @DeleteMapping("/deleteUser/{id}")
    @Operation(summary = "Delete User By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete User By Id", content = {@Content(mediaType = "application/json", schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        if (userService.deleteUserById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User Deleted SuccessFully.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No User Found With ID: " + id));
    }

}