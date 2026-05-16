package com.bn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.bn.enums.TransferStatus;

import lombok.Data;

@Data
public class TransferResponseDTO {

	private UUID id;
	private BigDecimal amount;
	private TransferStatus status;
	private UUID senderAccountId;
	private UUID receiverAccountId;
	private LocalDateTime createdAt;
}