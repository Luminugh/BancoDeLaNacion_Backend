package com.bn.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LoanRequestDTO {

	@NotNull
	@Positive
	private BigDecimal requestedAmount;

	@NotNull
	private Integer termMonths;

	@NotBlank
	private String purpose;

	@NotNull
	@Positive
	private BigDecimal monthlyIncome;

	@NotNull
	private UUID userId;
}