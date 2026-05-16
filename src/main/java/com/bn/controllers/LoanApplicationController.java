package com.bn.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bn.dto.request.LoanApplicationRequestDTO;
import com.bn.dto.response.ApiResponse;
import com.bn.dto.response.LoanApplicationResponseDTO;
import com.bn.services.interfaces.LoanApplicationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loan-applications")
@Validated
@RequiredArgsConstructor
@Tag(name = "Loan Applications", description = "Loan application endpoints")
public class LoanApplicationController {

	private final LoanApplicationService loanApplicationService;

	@GetMapping
	@Operation(summary = "List loan applications")
	public ResponseEntity<ApiResponse<List<LoanApplicationResponseDTO>>> findAll() {
		return ResponseEntity.ok(ApiResponse.success("Loan applications retrieved successfully", loanApplicationService.findAll()));
	}

	@PostMapping
	@Operation(summary = "Create loan application")
	public ResponseEntity<ApiResponse<LoanApplicationResponseDTO>> create(@Valid @RequestBody LoanApplicationRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Loan application created successfully", loanApplicationService.create(request)));
	}
}