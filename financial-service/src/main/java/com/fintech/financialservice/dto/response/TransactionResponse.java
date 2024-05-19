package com.fintech.financialservice.dto.response;

import com.fintech.financialservice.dto.TransactionDTO;

import java.util.List;

public record TransactionResponse(List<TransactionDTO> transactions) {
}
