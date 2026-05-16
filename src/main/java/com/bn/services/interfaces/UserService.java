package com.bn.services.interfaces;

import java.util.List;

import com.bn.dto.request.RegisterRequestDTO;
import com.bn.dto.response.UserResponseDTO;

public interface UserService {

	List<UserResponseDTO> findAll();

	UserResponseDTO create(RegisterRequestDTO request);
}