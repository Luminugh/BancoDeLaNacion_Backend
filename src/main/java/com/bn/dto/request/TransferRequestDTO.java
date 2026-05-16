package com.bn.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransferRequestDTO {

	@NotNull
	private UUID senderAccountId;

	@NotNull
	private UUID receiverAccountId;

	@NotNull
	@Positive
	private BigDecimal amount;

	private String description;
}