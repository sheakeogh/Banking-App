package com.bank.backend.controller;

import com.bank.backend.exception.InvalidRequestException;
import com.bank.backend.model.Transaction;
import com.bank.backend.model.TransactionRequest;
import com.bank.backend.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/createTransaction")
    @Operation(summary = "Create New Transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Create New Transaction", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = InvalidRequestException.class))} ),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = { @Content(schema = @Schema(hidden = true))} )
    })
    public ResponseEntity<?> createNewTransaction(@RequestBody TransactionRequest transactionRequest, HttpServletRequest request) {
        Transaction transaction = transactionService.createTransaction(transactionRequest, request);
        if (transaction == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new InvalidRequestException("Issue with Data Passed. Try Again!"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

}
