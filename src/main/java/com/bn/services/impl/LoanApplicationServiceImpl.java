package com.bn.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bn.dto.request.LoanApplicationRequestDTO;
import com.bn.dto.response.LoanApplicationResponseDTO;
import com.bn.entities.LoanApplication;
import com.bn.entities.User;
import com.bn.enums.ApplicationStatus;
import com.bn.exceptions.ResourceNotFoundException;
import com.bn.repositories.LoanApplicationRepository;
import com.bn.repositories.UserRepository;
import com.bn.services.interfaces.LoanApplicationService;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

	private final LoanApplicationRepository loanApplicationRepository;
	private final UserRepository userRepository;

	public LoanApplicationServiceImpl(LoanApplicationRepository loanApplicationRepository, UserRepository userRepository) {
		this.loanApplicationRepository = loanApplicationRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<LoanApplicationResponseDTO> findAll() {
		return loanApplicationRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public LoanApplicationResponseDTO create(LoanApplicationRequestDTO request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
		LoanApplication loanApplication = new LoanApplication();
		loanApplication.setRequestedAmount(request.getRequestedAmount());
		loanApplication.setTermMonths(request.getTermMonths());
		loanApplication.setPurpose(request.getPurpose());
		loanApplication.setMonthlyIncome(request.getMonthlyIncome());
		loanApplication.setStatus(ApplicationStatus.SUBMITTED);
		loanApplication.setUser(user);
		return toResponse(loanApplicationRepository.save(loanApplication));
	}

	private LoanApplicationResponseDTO toResponse(LoanApplication loanApplication) {
		LoanApplicationResponseDTO response = new LoanApplicationResponseDTO();
		response.setId(loanApplication.getId());
		response.setRequestedAmount(loanApplication.getRequestedAmount());
		response.setTermMonths(loanApplication.getTermMonths());
		response.setPurpose(loanApplication.getPurpose());
		response.setMonthlyIncome(loanApplication.getMonthlyIncome());
		response.setStatus(loanApplication.getStatus());
		response.setUserId(loanApplication.getUser() != null ? loanApplication.getUser().getId() : null);
		response.setCreatedAt(loanApplication.getCreatedAt());
		return response;
	}
}