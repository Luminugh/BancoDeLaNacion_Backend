package com.bn.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bn.dto.request.AccountRequestDTO;
import com.bn.dto.response.ApiResponse;
import com.bn.dto.response.AccountResponseDTO;
import com.bn.services.interfaces.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@Validated
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Account management endpoints")
public class AccountController {

	private final AccountService accountService;

	@GetMapping
	@Operation(summary = "List accounts")
	public ResponseEntity<ApiResponse<List<AccountResponseDTO>>> findAll() {
		return ResponseEntity.ok(ApiResponse.success("Accounts retrieved successfully", accountService.findAll()));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get account by id")
	public ResponseEntity<ApiResponse<AccountResponseDTO>> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(ApiResponse.success("Account retrieved successfully", accountService.findById(id)));
	}

	@PostMapping
	@Operation(summary = "Create account")
	public ResponseEntity<ApiResponse<AccountResponseDTO>> create(@Valid @RequestBody AccountRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Account created successfully", accountService.create(request)));
	}
}