package com.bn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.bn.enums.AccountStatus;

import lombok.Data;

@Data
public class AccountResponseDTO {

	private UUID id;
	private String accountNumber;
	private BigDecimal balance;
	private String currency;
	private AccountStatus status;
	private UUID userId;
	private LocalDateTime createdAt;
}