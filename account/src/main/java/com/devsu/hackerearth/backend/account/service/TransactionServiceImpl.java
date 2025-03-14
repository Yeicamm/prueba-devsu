package com.devsu.hackerearth.backend.account.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.config.AccountNotFound;
import com.devsu.hackerearth.backend.account.config.BalanceUnavailable;
import com.devsu.hackerearth.backend.account.config.TransactionNotFound;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Cuenta no encontrada.";
    private static final String TRANSACTION_NOT_FOUND = "Transaccion no encontrada.";
    private static final String BALANCE_UNAVAILABLE = "Saldo no disponible.";

    @Override
    public List<TransactionDto> getAll() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream().map(transaction -> {
            TransactionDto dto = new TransactionDto();
            BeanUtils.copyProperties(transaction, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public TransactionDto getById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFound(TRANSACTION_NOT_FOUND));
        TransactionDto dto = new TransactionDto();
        BeanUtils.copyProperties(transaction, dto);
        return dto;
    }

    @Override
    @Transactional
    public TransactionDto create(TransactionDto transactionDto) {
        Optional<Account> accountOpt = accountRepository.findById(transactionDto.getAccountId());

        if (accountOpt.isEmpty()) {
            throw new AccountNotFound(ACCOUNT_NOT_FOUND_MESSAGE);
        }

        Account account = accountOpt.get();

        if (transactionDto.getType().equalsIgnoreCase("retiro")
                && account.getInitialAmount() < transactionDto.getAmount()) {
            throw new BalanceUnavailable(BALANCE_UNAVAILABLE);
        }

        double newBalance = transactionDto.getType().equalsIgnoreCase("retiro")
                ? account.getInitialAmount() - transactionDto.getAmount()
                : account.getInitialAmount() + transactionDto.getAmount();

        account.setInitialAmount(newBalance);
        accountRepository.save(account);

        Transaction transaction = new Transaction();
        BeanUtils.copyProperties(transactionDto, transaction);
        transaction.setBalance(newBalance);
        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionDto savedTransactionDto = new TransactionDto();
        BeanUtils.copyProperties(savedTransaction, savedTransactionDto);

        return savedTransactionDto;
    }

    @Override
    public List<BankStatementDto> getAllByAccountClientIdAndDateBetween(Long clientId, Date dateTransactionStart,
            Date dateTransactionEnd) {
        List<Account> accounts = accountRepository.findByClientId(clientId);
        List<BankStatementDto> statements = new ArrayList<>();

        for (Account account : accounts) {
            List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());
            for (Transaction transaction : transactions) {
                if (transaction.getDate().after(dateTransactionStart)
                        && transaction.getDate().before(dateTransactionEnd)) {
                    BankStatementDto statement = new BankStatementDto(
                            transaction.getDate(),
                            "Cliente " + clientId,
                            account.getNumber(),
                            account.getType(),
                            account.getInitialAmount(),
                            account.isStatus(),
                            transaction.getType(),
                            transaction.getAmount(),
                            transaction.getBalance());
                    statements.add(statement);
                }
            }
        }
        return statements;
    }

    @Override
    public TransactionDto getLastByAccountId(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new AccountNotFound(ACCOUNT_NOT_FOUND_MESSAGE);
        }
        Transaction lastTransaction = transactionRepository.findLastTransaction(accountId);
        if (lastTransaction == null) {
            throw new TransactionNotFound(TRANSACTION_NOT_FOUND);
        }
        TransactionDto dto = new TransactionDto();
        BeanUtils.copyProperties(lastTransaction, dto);
        return dto;
    }

}
