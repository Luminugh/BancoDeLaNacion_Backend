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

import com.bn.dto.request.TransferRequestDTO;
import com.bn.dto.response.ApiResponse;
import com.bn.dto.response.TransferResponseDTO;
import com.bn.services.interfaces.TransferService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transfers")
@Validated
@RequiredArgsConstructor
@Tag(name = "Transfers", description = "Transfer endpoints")
public class TransferController {

	private final TransferService transferService;

	@PostMapping
	@Operation(summary = "Create transfer")
	public ResponseEntity<ApiResponse<TransferResponseDTO>> create(@Valid @RequestBody TransferRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Transfer created successfully", transferService.create(request)));
	}
}