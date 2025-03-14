package com.devsu.hackerearth.backend.account.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.devsu.hackerearth.backend.account.config.AccountNotFound;
import com.devsu.hackerearth.backend.account.config.ClientNotFound;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.ClientDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;
    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Cuenta no encontrada.";
    private static final String CLIENT_NOT_FOUND = "cliente no encontrado.";
    private static final String CLIENT_SERVICE_URL = "http://localhost:8001/api/clients";

    @Override
    public List<AccountDto> getAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(account -> {
            AccountDto dto = new AccountDto();
            BeanUtils.copyProperties(account, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public AccountDto getById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFound(ACCOUNT_NOT_FOUND_MESSAGE));
        AccountDto dto = new AccountDto();
        BeanUtils.copyProperties(account, dto);
        return dto;
    }

    @Override
    @Transactional
    public AccountDto create(AccountDto accountDto) {
        try {
            restTemplate.exchange(
                    CLIENT_SERVICE_URL + "/" + accountDto.getClientId(),
                    HttpMethod.GET,
                    null,
                    ClientDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ClientNotFound(CLIENT_NOT_FOUND);
        } catch (RestClientException e) {
            throw new RuntimeException("Error al conectar con el servicio de clientes: " + e.getMessage());
        }

        Account account = new Account();
        BeanUtils.copyProperties(accountDto, account);
        Account savedAccount = accountRepository.save(account);
        AccountDto savedDto = new AccountDto();
        BeanUtils.copyProperties(savedAccount, savedDto);
        return savedDto;
    }

    @Override
    @Transactional
    public AccountDto update(AccountDto accountDto) {
        Account account = accountRepository.findById(accountDto.getId())
                .orElseThrow(() -> new AccountNotFound(ACCOUNT_NOT_FOUND_MESSAGE));
        BeanUtils.copyProperties(accountDto, account, "id");
        Account updatedAccount = accountRepository.save(account);
        AccountDto updatedDto = new AccountDto();
        BeanUtils.copyProperties(updatedAccount, updatedDto);
        return updatedDto;
    }

    @Override
    @Transactional
    public AccountDto partialUpdate(Long id, PartialAccountDto partialAccountDto) {
        Optional<Account> existingAccountOpt = accountRepository.findById(id);

        if (existingAccountOpt.isPresent()) {
            Account existingAccount = existingAccountOpt.get();

            if (partialAccountDto != null) {
                existingAccount.setStatus(partialAccountDto.isStatus());
            }

            Account updatedAccount = accountRepository.save(existingAccount);

            AccountDto updatedAccountDto = new AccountDto();
            BeanUtils.copyProperties(updatedAccount, updatedAccountDto);

            return updatedAccountDto;
        }
        throw new AccountNotFound(ACCOUNT_NOT_FOUND_MESSAGE);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
        } else {
            throw new AccountNotFound(ACCOUNT_NOT_FOUND_MESSAGE);
        }
    }

}
