package com.bn.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.bn.dto.request.LoanRequestDTO;
import com.bn.dto.response.LoanResponseDTO;

public interface LoanService {

	List<LoanResponseDTO> findAll();

	LoanResponseDTO findById(UUID id);

	LoanResponseDTO create(LoanRequestDTO request);
}