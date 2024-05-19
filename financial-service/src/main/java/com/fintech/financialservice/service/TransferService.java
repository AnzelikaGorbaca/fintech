package com.fintech.financialservice.service;

import com.fintech.financialservice.dto.response.TransferResponse;
import com.fintech.financialservice.util.enums.Currency;

import java.math.BigDecimal;
/**
 * Service for transferring funds between accounts related operations.
 */
public interface TransferService {

    /**
     * Transfers a specified amount of funds from one account to another.
     *
     * @param fromAccountId The ID of the account from which funds are to be debited.
     * @param toAccountId The ID of the account to which funds are to be credited.
     * @param amount The amount of funds to be transferred.
     * @param transferCurrency The currency in which the transfer is to be made.
     * @return A TransferResponse containing details of the transaction.
     */
    TransferResponse transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount, Currency transferCurrency);
}
