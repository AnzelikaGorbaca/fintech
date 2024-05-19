package com.fintech.financialservice.rest.controller;

import com.fintech.financialservice.dto.request.TransferRequest;
import com.fintech.financialservice.dto.response.TransferResponse;
import com.fintech.financialservice.service.TransferService;
import com.fintech.financialservice.util.enums.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping("/accounts/transfer")
    public ResponseEntity<TransferResponse> transferFunds(@Valid @RequestBody TransferRequest transferRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transferService.transferFunds(
                        transferRequest.getFromAccountId(),
                        transferRequest.getToAccountId(),
                        transferRequest.getAmount(),
                        Currency.getCurrency(transferRequest.getTransferCurrency())));
    }
}
