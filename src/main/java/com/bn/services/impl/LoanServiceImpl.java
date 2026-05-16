package com.bn.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bn.dto.request.LoanRequestDTO;
import com.bn.dto.response.LoanResponseDTO;
import com.bn.entities.Loan;
import com.bn.entities.User;
import com.bn.enums.LoanStatus;
import com.bn.exceptions.ResourceNotFoundException;
import com.bn.repositories.LoanRepository;
import com.bn.repositories.UserRepository;
import com.bn.services.interfaces.LoanService;

@Service
public class LoanServiceImpl implements LoanService {

	private final LoanRepository loanRepository;
	private final UserRepository userRepository;

	public LoanServiceImpl(LoanRepository loanRepository, UserRepository userRepository) {
		this.loanRepository = loanRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<LoanResponseDTO> findAll() {
		return loanRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public LoanResponseDTO findById(UUID id) {
		Loan loan = loanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
		return toResponse(loan);
	}

	@Override
	public LoanResponseDTO create(LoanRequestDTO request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
		Loan loan = new Loan();
		loan.setLoanNumber("LN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		loan.setAmount(request.getRequestedAmount());
		loan.setInterestRate(BigDecimal.ZERO);
		loan.setTermMonths(request.getTermMonths());
		loan.setMonthlyPayment(BigDecimal.ZERO);
		loan.setRemainingBalance(request.getRequestedAmount());
		loan.setStatus(LoanStatus.ACTIVE);
		loan.setUser(user);
		return toResponse(loanRepository.save(loan));
	}

	private LoanResponseDTO toResponse(Loan loan) {
		LoanResponseDTO response = new LoanResponseDTO();
		response.setId(loan.getId());
		response.setLoanNumber(loan.getLoanNumber());
		response.setAmount(loan.getAmount());
		response.setInterestRate(loan.getInterestRate());
		response.setTermMonths(loan.getTermMonths());
		response.setMonthlyPayment(loan.getMonthlyPayment());
		response.setRemainingBalance(loan.getRemainingBalance());
		response.setStatus(loan.getStatus());
		response.setDisbursementDate(loan.getDisbursementDate());
		response.setUserId(loan.getUser() != null ? loan.getUser().getId() : null);
		response.setCreatedAt(loan.getCreatedAt());
		return response;
	}
}