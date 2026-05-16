package com.bn.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.bn.enums.LoanStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "loans")
public class Loan extends BaseEntity {

	@Column(name = "loan_number", nullable = false, unique = true, length = 30)
	private String loanNumber;

	@Column(nullable = false, precision = 14, scale = 2)
	private BigDecimal amount;

	@Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
	private BigDecimal interestRate;

	@Column(name = "term_months", nullable = false)
	private Integer termMonths;

	@Column(name = "monthly_payment", precision = 14, scale = 2)
	private BigDecimal monthlyPayment;

	@Column(name = "remaining_balance", precision = 14, scale = 2)
	private BigDecimal remainingBalance;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private LoanStatus status;

	@Column(name = "disbursement_date")
	private LocalDate disbursementDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}