package com.bn.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

	@Value("${bn.security.jwt.secret:change-me-later}")
	private String secret;

	@Value("${bn.security.jwt.expiration-ms:86400000}")
	private long expirationMs;

	public String generateToken(String username) {
		long issuedAt = Instant.now().getEpochSecond();
		long expiresAt = issuedAt + (expirationMs / 1000L);
		String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
		String payload = base64Url("{\"sub\":\"" + escapeJson(username) + "\",\"iat\":" + issuedAt + ",\"exp\":" + expiresAt + "}");
		String signature = sign(header + "." + payload);
		return header + "." + payload + "." + signature;
	}

	public String extractUsername(String token) {
		if (token == null || token.isBlank()) {
			return null;
		}

		try {
			String[] parts = token.split("\\.");
			if (parts.length != 3) {
				return null;
			}

			String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
			return extractStringValue(payloadJson, "sub");
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	public boolean isTokenValid(String token, String username) {
		if (token == null || token.isBlank() || username == null || username.isBlank()) {
			return false;
		}

		try {
			String extractedUsername = extractUsername(token);
			if (extractedUsername == null || !extractedUsername.equalsIgnoreCase(username)) {
				return false;
			}

			String[] parts = token.split("\\.");
			if (parts.length != 3) {
				return false;
			}

			if (!sign(parts[0] + "." + parts[1]).equals(parts[2])) {
				return false;
			}

			String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
			Long exp = extractLongValue(payloadJson, "exp");
			return exp != null && exp > Instant.now().getEpochSecond();
		} catch (Exception ex) {
			return false;
		}
	}

	private String sign(String data) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			mac.init(keySpec);
			byte[] signature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
			return Base64.getUrlEncoder().withoutPadding().encodeToString(signature);
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to sign JWT token", ex);
		}
	}

	private String base64Url(String value) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes(StandardCharsets.UTF_8));
	}

	private String escapeJson(String value) {
		return value.replace("\\", "\\\\").replace("\"", "\\\"");
	}

	private String extractStringValue(String json, String key) {
		String marker = "\"" + key + "\":\"";
		int start = json.indexOf(marker);
		if (start < 0) {
			return null;
		}
		int valueStart = start + marker.length();
		int valueEnd = json.indexOf('"', valueStart);
		if (valueEnd < 0) {
			return null;
		}
		return json.substring(valueStart, valueEnd).replace("\\\"", "\"").replace("\\\\", "\\");
	}

	private Long extractLongValue(String json, String key) {
		String marker = "\"" + key + "\":";
		int start = json.indexOf(marker);
		if (start < 0) {
			return null;
		}
		int valueStart = start + marker.length();
		int valueEnd = valueStart;
		while (valueEnd < json.length() && Character.isDigit(json.charAt(valueEnd))) {
			valueEnd++;
		}
		if (valueEnd == valueStart) {
			return null;
		}
		return Long.parseLong(json.substring(valueStart, valueEnd));
	}
}