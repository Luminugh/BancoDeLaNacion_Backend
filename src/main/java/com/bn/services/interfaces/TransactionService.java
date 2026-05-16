package com.bn.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.bn.dto.response.TransactionResponseDTO;

public interface TransactionService {

	List<TransactionResponseDTO> findAll();

	List<TransactionResponseDTO> findByAccountId(UUID accountId);
}