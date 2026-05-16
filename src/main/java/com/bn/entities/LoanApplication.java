package com.bn.entities;

import java.math.BigDecimal;

import com.bn.enums.ApplicationStatus;

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
@Table(name = "loan_applications")
public class LoanApplication extends BaseEntity {

	@Column(name = "requested_amount", nullable = false, precision = 14, scale = 2)
	private BigDecimal requestedAmount;

	@Column(name = "term_months", nullable = false)
	private Integer termMonths;

	@Column(columnDefinition = "text")
	private String purpose;

	@Column(name = "monthly_income", precision = 14, scale = 2)
	private BigDecimal monthlyIncome;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ApplicationStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
}