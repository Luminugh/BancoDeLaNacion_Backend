package com.bn.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bn.dto.request.LoginRequestDTO;
import com.bn.dto.request.RegisterRequestDTO;
import com.bn.dto.response.ApiResponse;
import com.bn.dto.response.AuthResponseDTO;
import com.bn.services.interfaces.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@Validated
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	@Operation(summary = "Register user")
	public ResponseEntity<ApiResponse<AuthResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("User registered successfully", authService.register(request)));
	}

	@PostMapping("/login")
	@Operation(summary = "Login user")
	public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
		return ResponseEntity.ok(ApiResponse.success("Login successful", authService.login(request)));
	}
}