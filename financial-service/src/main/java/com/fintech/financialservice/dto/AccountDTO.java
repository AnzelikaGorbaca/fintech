package com.fintech.financialservice.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@JsonPropertyOrder({ "userAccountId", "currency", "balance"})
public class AccountDTO {

    private Long userAccountId;
    private String currency;
    private BigDecimal balance;
}
