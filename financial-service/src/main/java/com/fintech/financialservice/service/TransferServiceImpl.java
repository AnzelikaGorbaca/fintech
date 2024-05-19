package com.fintech.financialservice.service;

import com.fintech.financialservice.dto.response.TransferResponse;
import com.fintech.financialservice.error.ErrorClassification;
import com.fintech.financialservice.error.exception.FinancialServiceException;
import com.fintech.financialservice.persistance.model.Account;
import com.fintech.financialservice.persistance.model.Transaction;
import com.fintech.financialservice.persistance.repository.AccountRepository;
import com.fintech.financialservice.persistance.repository.TransactionRepository;
import com.fintech.financialservice.util.enums.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.fintech.financialservice.util.mapper.TransactionMapper.toDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CurrencyConversionService currencyConversionService;

    @Override
    public TransferResponse transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount, Currency transferCurrency) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new FinancialServiceException(
                    ErrorClassification.FINANCIALSERVICE_INVALID_REQUEST_ERROR,
                    "Transfer amount must be positive.");
        }

        final Account fromAccount = getAccount(fromAccountId, "From account not found");
        final Account toAccount = getAccount(toAccountId, "To account not found");

        if (transferCurrency != toAccount.getCurrency()) {
            throw new FinancialServiceException(
                    ErrorClassification.FINANCIALSERVICE_INVALID_REQUEST_ERROR,
                    String.format("Transfer currency %s does not match receiver's account currency %s",
                            transferCurrency,
                            toAccount.getCurrency()));
        }

        amount = convertAmountIfRequired(fromAccount, toAccount, amount);
        performTransfer(fromAccount, toAccount, amount);
        logTransactions(fromAccount, toAccount, amount, transferCurrency);

        return new TransferResponse(
                toDTO(new Transaction(fromAccount, amount, transferCurrency, toAccountId)));
    }

    private BigDecimal convertAmountIfRequired(Account fromAccount, Account toAccount, BigDecimal amount) {
        if (fromAccount.getCurrency() == toAccount.getCurrency()) {
            return amount;
        }
        log.info("Converting amount from {} to {}", fromAccount.getCurrency(), toAccount.getCurrency());
        return currencyConversionService.convert(fromAccount.getCurrency(), toAccount.getCurrency(), amount);
    }

    private Account getAccount(Long accountId, String errorMessage) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new FinancialServiceException(
                        ErrorClassification.FINANCIALSERVICE_CLIENT_NOT_FOUND,
                        errorMessage));
    }

    private void performTransfer(Account fromAccount, Account toAccount, BigDecimal amount) {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new FinancialServiceException(
                    ErrorClassification.FINANCIALSERVICE_INVALID_REQUEST_ERROR,
                    "Insufficient funds in the source account.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        log.info("Transferring {} from account {} to {}", amount, fromAccount.getId(), toAccount.getId());
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    private void logTransactions(Account fromAccount, Account toAccount, BigDecimal amount, Currency transferCurrency) {
        Transaction debitTransaction = new Transaction(fromAccount, amount.negate(), transferCurrency, toAccount.getId());
        Transaction creditTransaction = new Transaction(toAccount, amount, transferCurrency, fromAccount.getId());

        log.info("Logging transactions: Debit: {}, Credit: {}", debitTransaction, creditTransaction);
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
    }
}
