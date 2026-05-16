package com.bn.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bn.dto.request.TransferRequestDTO;
import com.bn.dto.response.TransferResponseDTO;
import com.bn.enums.TransferStatus;
import com.bn.services.interfaces.TransferService;

@Service
public class TransferServiceImpl implements TransferService {

	@Override
	public List<TransferResponseDTO> findAll() {
		return List.of();
	}

	@Override
	public TransferResponseDTO create(TransferRequestDTO request) {
		TransferResponseDTO response = new TransferResponseDTO();
		response.setId(UUID.randomUUID());
		response.setAmount(request.getAmount());
		response.setStatus(TransferStatus.PENDING);
		response.setSenderAccountId(request.getSenderAccountId());
		response.setReceiverAccountId(request.getReceiverAccountId());
		response.setCreatedAt(LocalDateTime.now());
		return response;
	}
}