package com.bn.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bn.dto.request.RegisterRequestDTO;
import com.bn.dto.response.ApiResponse;
import com.bn.dto.response.ProfileResponseDTO;
import com.bn.services.interfaces.ProfileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profiles")
@Validated
@RequiredArgsConstructor
@Tag(name = "Profiles", description = "Profile management endpoints")
public class ProfileController {

	private final ProfileService profileService;

	@GetMapping
	@Operation(summary = "List profiles")
	public ResponseEntity<ApiResponse<List<ProfileResponseDTO>>> findAll() {
		return ResponseEntity.ok(ApiResponse.success("Profiles retrieved successfully", profileService.findAll()));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get profile by id")
	public ResponseEntity<ApiResponse<ProfileResponseDTO>> findById(@PathVariable UUID id) {
		return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", profileService.findById(id)));
	}

	@PostMapping
	@Operation(summary = "Create profile")
	public ResponseEntity<ApiResponse<ProfileResponseDTO>> create(@Valid @RequestBody RegisterRequestDTO request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("Profile created successfully", profileService.create(request)));
	}
}