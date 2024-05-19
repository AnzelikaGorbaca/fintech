package com.fintech.financialservice.util.mapper;

import com.fintech.financialservice.dto.AccountDTO;
import com.fintech.financialservice.persistance.model.Account;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public final class AccountMapper {

    public static AccountDTO toDTO(Account account) {
        return account == null
                ? null
                : AccountDTO.builder()
                        .userAccountId(account.getId())
                        .currency(account.getCurrency().getSymbol())
                        .balance(account.getBalance())
                        .build();
    }
}
