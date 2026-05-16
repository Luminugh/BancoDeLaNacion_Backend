package com.bn.services.impl;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bn.dto.request.LoginRequestDTO;
import com.bn.dto.request.RegisterRequestDTO;
import com.bn.dto.response.AuthResponseDTO;
import com.bn.dto.response.ProfileResponseDTO;
import com.bn.entities.User;
import com.bn.exceptions.BadRequestException;
import com.bn.exceptions.ResourceNotFoundException;
import com.bn.repositories.UserRepository;
import com.bn.security.JwtUtil;
import com.bn.services.interfaces.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public AuthResponseDTO register(RegisterRequestDTO request) {
		User user = new User();
		user.setDni(request.getDni());
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setPhone(request.getPhone());
		user.setRole(request.getRole());

		User savedUser = userRepository.save(user);
		return buildResponse(savedUser, jwtUtil.generateToken(savedUser.getEmail()));
	}

	@Override
	public AuthResponseDTO login(LoginRequestDTO request) {
		User user = userRepository.findByEmailIgnoreCase(request.getEmail().trim())
				.orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadRequestException("Invalid credentials");
		}

		return buildResponse(user, jwtUtil.generateToken(user.getEmail().toLowerCase(Locale.ROOT)));
	}

	private AuthResponseDTO buildResponse(User user, String token) {
		ProfileResponseDTO profile = new ProfileResponseDTO();
		profile.setId(user.getId());
		profile.setDni(user.getDni());
		profile.setFirstName(user.getFirstName());
		profile.setLastName(user.getLastName());
		profile.setEmail(user.getEmail());
		profile.setPhone(user.getPhone());
		profile.setRole(user.getRole());
		profile.setCreatedAt(user.getCreatedAt());

		AuthResponseDTO response = new AuthResponseDTO();
		response.setAccessToken(token);
		response.setTokenType("Bearer");
		response.setProfile(profile);
		return response;
	}
}