package com.bn.services.interfaces;

import java.util.List;

import com.bn.dto.request.TransferRequestDTO;
import com.bn.dto.response.TransferResponseDTO;

public interface TransferService {

	List<TransferResponseDTO> findAll();

	TransferResponseDTO create(TransferRequestDTO request);
}