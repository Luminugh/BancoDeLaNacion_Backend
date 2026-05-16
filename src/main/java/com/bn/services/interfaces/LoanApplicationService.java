package com.bn.services.interfaces;

import java.util.List;

import com.bn.dto.request.LoanApplicationRequestDTO;
import com.bn.dto.response.LoanApplicationResponseDTO;

public interface LoanApplicationService {

	List<LoanApplicationResponseDTO> findAll();

	LoanApplicationResponseDTO create(LoanApplicationRequestDTO request);
}