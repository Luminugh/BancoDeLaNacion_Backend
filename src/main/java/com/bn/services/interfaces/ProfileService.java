package com.bn.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.bn.dto.request.RegisterRequestDTO;
import com.bn.dto.response.ProfileResponseDTO;

public interface ProfileService {

	List<ProfileResponseDTO> findAll();

	ProfileResponseDTO findById(UUID id);

	ProfileResponseDTO create(RegisterRequestDTO request);
}