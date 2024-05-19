package com.fintech.financialservice.service;

import com.fintech.financialservice.dto.AccountDTO;
import com.fintech.financialservice.dto.TransactionDTO;
import com.fintech.financialservice.error.exception.FinancialServiceException;
import com.fintech.financialservice.persistance.model.Account;
import com.fintech.financialservice.persistance.model.Transaction;
import com.fintech.financialservice.persistance.repository.AccountRepository;
import com.fintech.financialservice.persistance.repository.ClientRepository;
import com.fintech.financialservice.persistance.repository.TransactionRepository;
import com.fintech.financialservice.util.mapper.AccountMapper;
import com.fintech.financialservice.util.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.fintech.financialservice.error.ErrorClassification.FINANCIALSERVICE_CLIENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AccountDTO> getAccountsByClientId(Long clientId) {
        log.info("Fetching accounts for client ID: {}", clientId);

        validateClient(clientId);
        List<Account> accounts = accountRepository.findByClientId(clientId);

        return accounts.stream()
                .map(AccountMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public  List<TransactionDTO> getTransactionsByAccountId(Long accountId, int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "timestamp"));
        Page<Transaction> page = transactionRepository.findByAccountId(accountId, pageRequest);

        return page.getContent().stream()
                .map(TransactionMapper::toDTO)
                .toList();
    }

    private void validateClient(Long clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new FinancialServiceException(FINANCIALSERVICE_CLIENT_NOT_FOUND);
        }
    }
}
