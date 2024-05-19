package com.fintech.financialservice.service;

import com.fintech.financialservice.dto.AccountDTO;
import com.fintech.financialservice.dto.TransactionDTO;

import java.util.List;
/**
 * Service for account-related operations.
 */
public interface AccountService {

    /**
     * Retrieves all accounts for a given client ID.
     *
     * @param clientId The ID of the client whose accounts to be retrieved.
     * @return A list of AccountDTO objects representing the accounts.
     */
    List<AccountDTO> getAccountsByClientId(Long clientId);

    /**
     * Retrieves transaction history for a given account, with pagination.
     *
     * @param accountId The ID of the account whose transactions to be fetched.
     * @param offset The pagination offset.
     * @param limit The max number of transactions to retrieve.
     * @return A list of TransactionDTO objects representing the transactions.
     */
    List<TransactionDTO> getTransactionsByAccountId(Long accountId, int offset, int limit);
}
