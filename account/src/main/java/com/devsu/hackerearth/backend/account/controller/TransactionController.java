package com.devsu.hackerearth.backend.account.controller;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsu.hackerearth.backend.account.model.dto.BankStatementDto;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import com.devsu.hackerearth.backend.account.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping
	public ResponseEntity<List<TransactionDto>> getAll() {
		return ResponseEntity.ok(transactionService.getAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<TransactionDto> get(@PathVariable Long id) {
		return ResponseEntity.ok(transactionService.getById(id));
	}

	@PostMapping
	public ResponseEntity<TransactionDto> create(@RequestBody TransactionDto transactionDto) {
		TransactionDto createdTransaction = transactionService.create(transactionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
	}

    @GetMapping("/clients/{clientId}/report")
	public ResponseEntity<List<BankStatementDto>> report(@PathVariable Long clientId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionStart,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateTransactionEnd) {
				return ResponseEntity.ok(transactionService
				.getAllByAccountClientIdAndDateBetween(clientId, dateTransactionStart, dateTransactionEnd));
	}
	
	@GetMapping("/accounts/{accountId}/last")
    public ResponseEntity<TransactionDto> getLastTransaction(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getLastByAccountId(accountId));
    }
}
