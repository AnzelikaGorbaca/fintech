package com.fintech.financialservice.rest.controller;

import com.fintech.financialservice.dto.AccountDTO;
import com.fintech.financialservice.dto.TransactionDTO;
import com.fintech.financialservice.dto.response.AccountResponse;
import com.fintech.financialservice.dto.response.TransactionResponse;
import com.fintech.financialservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/clients/{clientId}/accounts")
    public ResponseEntity<AccountResponse> getAccounts(@PathVariable Long clientId) {

        List<AccountDTO> accounts = accountService.getAccountsByClientId(clientId);
        return ResponseEntity.status(accounts.isEmpty()
                        ? HttpStatus.NO_CONTENT
                        : HttpStatus.OK)
                .body(new AccountResponse(accounts));
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<TransactionResponse> getTransactions(@PathVariable Long accountId,
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit) {

        List<TransactionDTO> transactions = accountService.getTransactionsByAccountId(accountId, offset, limit);
        return ResponseEntity.status(transactions.isEmpty()
                        ? HttpStatus.NO_CONTENT
                        : HttpStatus.OK)
                .body(new TransactionResponse(transactions));
    }
}
