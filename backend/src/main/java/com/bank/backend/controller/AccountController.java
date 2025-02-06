package com.bank.backend.controller;

import com.bank.backend.exception.InvalidRequestException;
import com.bank.backend.model.Account;
import com.bank.backend.model.AccountRequest;
import com.bank.backend.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/createAccount")
    @Operation(summary = "Create New Account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create New Account", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> createNewAccount(@RequestBody AccountRequest accountRequest) {
        Account account = accountService.createAccount(accountRequest);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Issue with Data Passed. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping("/getAccount/{id}")
    @Operation(summary = "Get Account By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Account By ID", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("No Account Found. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @GetMapping("/getAllAccounts")
    @Operation(summary = "Get All Accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Accounts", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Account.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> getAllAccounts() {
        List<Account> accountList = accountService.getAllAccounts();
        if (accountList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Cannot Find Any Accounts. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(accountList);
    }

    @DeleteMapping("/deleteAccount/{id}")
    @Operation(summary = "Delete Account By ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete Account By ID", content = {@Content(mediaType = "application/json", schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> deleteAccountById(@PathVariable Long id) {
        if (accountService.deleteAccountById(id)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Account Has Been Deleted.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Issue with Data Passed. Try Again!"));
    }

}
