package com.fintech.financialservice.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Getter
@Setter
@Builder
public class TransferRequest {

    @NotNull(message = "From account ID must not be null")
    private Long fromAccountId;

    @NotNull(message = "To account ID must not be null")
    private Long toAccountId;

    @NotBlank(message = "Transfer currency must not be blank")
    private String transferCurrency;

    @NotNull(message = "Amount is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "Transfer amount must be greater than zero")
    private BigDecimal amount;
}
