package com.bn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.bn.enums.TransactionType;

import lombok.Data;

@Data
public class TransactionResponseDTO {

	private UUID id;
	private TransactionType transactionType;
	private BigDecimal amount;
	private String description;
	private UUID accountId;
	private LocalDateTime createdAt;
}