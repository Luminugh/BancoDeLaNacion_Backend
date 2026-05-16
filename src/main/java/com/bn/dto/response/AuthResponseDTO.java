package com.bn.dto.response;

import lombok.Data;

@Data
public class AuthResponseDTO {

	private String accessToken;
	private String tokenType;
	private ProfileResponseDTO profile;
}