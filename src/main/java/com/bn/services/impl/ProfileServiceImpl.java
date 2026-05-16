package com.bn.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bn.dto.request.RegisterRequestDTO;
import com.bn.dto.response.ProfileResponseDTO;
import com.bn.entities.User;
import com.bn.enums.UserRole;
import com.bn.exceptions.ResourceNotFoundException;
import com.bn.repositories.UserRepository;
import com.bn.services.interfaces.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public ProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<ProfileResponseDTO> findAll() {
		return userRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public ProfileResponseDTO findById(UUID id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
		return toResponse(user);
	}

	@Override
	public ProfileResponseDTO create(RegisterRequestDTO request) {
		User user = new User();
		user.setDni(request.getDni());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPhone(request.getPhone());
		user.setRole(request.getRole() != null ? request.getRole() : UserRole.CUSTOMER);
		return toResponse(userRepository.save(user));
	}

	private ProfileResponseDTO toResponse(User user) {
		ProfileResponseDTO response = new ProfileResponseDTO();
		response.setId(user.getId());
		response.setDni(user.getDni());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setEmail(user.getEmail());
		response.setPhone(user.getPhone());
		response.setRole(user.getRole());
		response.setCreatedAt(user.getCreatedAt());
		return response;
	}
}