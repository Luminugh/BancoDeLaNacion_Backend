package com.bn.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.bn.dto.request.AccountRequestDTO;
import com.bn.dto.response.AccountResponseDTO;

public interface AccountService {

	List<AccountResponseDTO> findAll();

	AccountResponseDTO findById(UUID id);

	AccountResponseDTO create(AccountRequestDTO request);
}