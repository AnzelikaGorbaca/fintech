package com.fintech.financialservice.util.mapper;

import com.fintech.financialservice.dto.TransactionDTO;
import com.fintech.financialservice.persistance.model.Transaction;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public final class TransactionMapper {

    public static TransactionDTO toDTO(Transaction transaction) {
        return transaction == null
                ? null
                : TransactionDTO.builder()
                    .accountId(transaction.getAccount().getId())
                    .amount(transaction.getAmount())
                    .currency(transaction.getCurrency())
                    .transactionAccountId(transaction.getTransactionAccount())
                    .timestamp(transaction.getTimestamp())
                    .build();
    }
}
