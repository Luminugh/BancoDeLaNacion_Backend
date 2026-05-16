package com.bn.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bn.dto.request.AccountRequestDTO;
import com.bn.dto.response.AccountResponseDTO;
import com.bn.entities.Account;
import com.bn.entities.User;
import com.bn.enums.AccountStatus;
import com.bn.exceptions.ResourceNotFoundException;
import com.bn.repositories.AccountRepository;
import com.bn.repositories.UserRepository;
import com.bn.services.interfaces.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final UserRepository userRepository;

	public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
		this.accountRepository = accountRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<AccountResponseDTO> findAll() {
		return accountRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public AccountResponseDTO findById(UUID id) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
		return toResponse(account);
	}

	@Override
	public AccountResponseDTO create(AccountRequestDTO request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Profile not found"));
		Account account = new Account();
		account.setAccountNumber(request.getAccountNumber());
		account.setBalance(BigDecimal.ZERO);
		account.setCurrency(request.getCurrency());
		account.setStatus(AccountStatus.ACTIVE);
		account.setUser(user);
		return toResponse(accountRepository.save(account));
	}

	private AccountResponseDTO toResponse(Account account) {
		AccountResponseDTO response = new AccountResponseDTO();
		response.setId(account.getId());
		response.setAccountNumber(account.getAccountNumber());
		response.setBalance(account.getBalance());
		response.setCurrency(account.getCurrency());
		response.setStatus(account.getStatus());
		response.setUserId(account.getUser() != null ? account.getUser().getId() : null);
		response.setCreatedAt(account.getCreatedAt());
		return response;
	}
}