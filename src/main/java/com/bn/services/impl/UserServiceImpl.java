package com.bn.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bn.dto.request.RegisterRequestDTO;
import com.bn.dto.response.UserResponseDTO;
import com.bn.enums.UserRole;
import com.bn.services.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public List<UserResponseDTO> findAll() {
		return List.of();
	}

	@Override
	public UserResponseDTO create(RegisterRequestDTO request) {
		UserResponseDTO response = new UserResponseDTO();
		response.setId(UUID.randomUUID());
		response.setDni(request.getDni());
		response.setFirstName(request.getFirstName());
		response.setLastName(request.getLastName());
		response.setEmail(request.getEmail());
		response.setPhone(request.getPhone());
		response.setRole(request.getRole() != null ? request.getRole() : UserRole.CUSTOMER);
		response.setCreatedAt(LocalDateTime.now());
		return response;
	}
}