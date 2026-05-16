package com.bn.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.bn.enums.UserRole;

import lombok.Data;

@Data
public class ProfileResponseDTO {

	private UUID id;
	private String dni;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private UserRole role;
	private LocalDateTime createdAt;
}