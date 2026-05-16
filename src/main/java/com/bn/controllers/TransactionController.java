package com.bn.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bn.dto.response.ApiResponse;
import com.bn.dto.response.TransactionResponseDTO;
import com.bn.services.interfaces.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@Validated
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction lookup endpoints")
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping
	@Operation(summary = "List transactions")
	public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> findAll() {
		return ResponseEntity.ok(ApiResponse.success("Transactions retrieved successfully", transactionService.findAll()));
	}

	@GetMapping("/account/{accountId}")
	@Operation(summary = "List transactions by account")
	public ResponseEntity<ApiResponse<List<TransactionResponseDTO>>> findByAccountId(@PathVariable UUID accountId) {
		return ResponseEntity.ok(ApiResponse.success("Account transactions retrieved successfully", transactionService.findByAccountId(accountId)));
	}
}