package com.fintech.financialservice.dto.response;

import com.fintech.financialservice.dto.AccountDTO;

import java.util.List;

public record AccountResponse(List<AccountDTO> accounts) {
}