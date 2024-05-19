package com.fintech.financialservice;

import com.fintech.financialservice.dto.AccountDTO;
import com.fintech.financialservice.dto.TransactionDTO;
import com.fintech.financialservice.persistance.model.Account;
import com.fintech.financialservice.persistance.model.Client;
import com.fintech.financialservice.util.enums.Currency;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public final class FinancialServiceTestHelper {

    public static final Long CLIENT_1_ID = 1L;
    public static final Long ACCOUNT_1_ID = 11L;
    public static final Long ACCOUNT_2_ID = 22L;
    public static final Long ACCOUNT_3_ID = 33L;
    public static final int DEFAULT_LIMIT = 10;
    public static final int DEFAULT_OFFSET = 0;
    public static final String PATH_PARAM_OFFSET = "offset";
    public static final String PATH_PARAM_LIMIT = "limit";

    public static TransactionDTO buildTransactionDTO(Long accountId, Long transactionAccountId, int amount, Currency currency) {
        return TransactionDTO.builder()
                .accountId(accountId)
                .amount(BigDecimal.valueOf(amount))
                .currency(currency)
                .transactionAccountId(transactionAccountId)
                .build();
    }

    public static AccountDTO buildAccountDTO(Long userAccountId, int balance, String currency) {
        return AccountDTO.builder()
                .userAccountId(userAccountId)
                .currency(currency)
                .balance(BigDecimal.valueOf(balance))
                .build();
    }

    public static Account buildAccount(Long accountId, int balance, Client client, Currency currency) {
        return Account.builder()
                .id(accountId)
                .currency(currency)
                .balance(BigDecimal.valueOf(balance))
                .client(client)
                .build();
    }
}
