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

import com.bn.dto.request.LoanRequestDTO;
import com.bn.dto.response.ApiResponse;
import com.bn.dto.response.LoanResponseDTO;
import com.bn.services.interfaces.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@Validated
@RequiredArgsConstructor
@Tag(name = "Loans", description = "Loan endpoints")
public class LoanController {

	private final LoanService loanService;

	@GetMapping
	@Operation(summary = "List loans")
	public ResponseEntity<ApiResponse<List<LoanResponseDTO>>> findAll() {
		return ResponseEntity.ok(ApiResponse.success("Loans retrieved successfully", loanService.findAll()));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get loan by id")
	public ResponseEntity<ApiResponse<LoanResponseDTO>> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(ApiResponse.success("Loan retrieved successfully", loanService.findById(id)));
	}

	@PostMapping
	@Operation(summary = "Create loan")
	public ResponseEntity<ApiResponse<LoanResponseDTO>> create(@Valid @RequestBody LoanRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Loan created successfully", loanService.create(request)));
	}
}