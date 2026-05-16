package com.bn.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bn.dto.response.TransactionResponseDTO;
import com.bn.entities.Transaction;
import com.bn.repositories.TransactionRepository;
import com.bn.services.interfaces.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepository;

	public TransactionServiceImpl(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	@Override
	public List<TransactionResponseDTO> findAll() {
		return transactionRepository.findAll().stream().map(this::toResponse).toList();
	}

	@Override
	public List<TransactionResponseDTO> findByAccountId(UUID accountId) {
		return transactionRepository.findAll().stream()
				.filter(transaction -> transaction.getAccount() != null && accountId.equals(transaction.getAccount().getId()))
				.map(this::toResponse)
				.toList();
	}

	private TransactionResponseDTO toResponse(Transaction transaction) {
		TransactionResponseDTO response = new TransactionResponseDTO();
		response.setId(transaction.getId());
		response.setTransactionType(transaction.getTransactionType());
		response.setAmount(transaction.getAmount());
		response.setDescription(transaction.getDescription());
		response.setAccountId(transaction.getAccount() != null ? transaction.getAccount().getId() : null);
		response.setCreatedAt(transaction.getCreatedAt());
		return response;
	}
}