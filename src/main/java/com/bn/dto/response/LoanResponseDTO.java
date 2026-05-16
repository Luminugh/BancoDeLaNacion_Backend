package com.bn.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.bn.enums.LoanStatus;

import lombok.Data;

@Data
public class LoanResponseDTO {

	private UUID id;
	private String loanNumber;
	private BigDecimal amount;
	private BigDecimal interestRate;
	private Integer termMonths;
	private BigDecimal monthlyPayment;
	private BigDecimal remainingBalance;
	private LoanStatus status;
	private LocalDate disbursementDate;
	private UUID userId;
	private LocalDateTime createdAt;
}