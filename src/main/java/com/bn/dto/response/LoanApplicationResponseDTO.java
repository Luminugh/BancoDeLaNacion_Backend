package com.bn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.bn.enums.ApplicationStatus;

import lombok.Data;

@Data
public class LoanApplicationResponseDTO {

	private UUID id;
	private BigDecimal requestedAmount;
	private Integer termMonths;
	private String purpose;
	private BigDecimal monthlyIncome;
	private ApplicationStatus status;
	private UUID userId;
	private LocalDateTime createdAt;
}