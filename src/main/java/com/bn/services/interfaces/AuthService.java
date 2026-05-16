package com.bn.services.interfaces;

import com.bn.dto.request.LoginRequestDTO;
import com.bn.dto.request.RegisterRequestDTO;
import com.bn.dto.response.AuthResponseDTO;

public interface AuthService {

	AuthResponseDTO register(RegisterRequestDTO request);

	AuthResponseDTO login(LoginRequestDTO request);
}