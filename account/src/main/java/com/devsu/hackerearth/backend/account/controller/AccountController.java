package com.devsu.hackerearth.backend.account.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import com.devsu.hackerearth.backend.account.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@GetMapping
	public ResponseEntity<List<AccountDto>> getAll() {
		return ResponseEntity.ok(accountService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(accountService.getById(id));
	}

	@PostMapping
	public ResponseEntity<AccountDto> create(@RequestBody AccountDto accountDto) {
		AccountDto createdAccount = accountService.create(accountDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
	}
	
    @PutMapping("/{id}")
	public ResponseEntity<AccountDto> update(@PathVariable Long id, @RequestBody AccountDto accountDto) {
		accountDto.setId(id);
		AccountDto updatedAccount = accountService.update(accountDto);
		return ResponseEntity.ok(updatedAccount);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<AccountDto> partialUpdate(@PathVariable Long id,
			@RequestBody PartialAccountDto partialAccountDto) {
				AccountDto updatedAccount = accountService.partialUpdate(id, partialAccountDto);
				return ResponseEntity.ok(updatedAccount);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		accountService.deleteById(id);
        return ResponseEntity.noContent().build();
	}
}
