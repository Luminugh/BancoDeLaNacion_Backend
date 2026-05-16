package com.bn.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountRequestDTO {

	@NotBlank
	private String accountNumber;

	@NotBlank
	private String currency;

	@NotNull
	private UUID userId;
}