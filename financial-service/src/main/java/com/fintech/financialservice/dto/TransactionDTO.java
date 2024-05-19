package com.fintech.financialservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fintech.financialservice.util.enums.Currency;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@JsonPropertyOrder({ "userAccountId", "transactionAccount", "transactionCurrency", "amount", "timestamp" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    @JsonProperty ("userAccountId")
    private Long accountId;
    private Long transactionAccountId;
    @JsonProperty ("transactionCurrency")
    private Currency currency;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal amount;
    private Instant timestamp;
}
