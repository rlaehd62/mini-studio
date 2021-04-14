package com.github.rlaehd62.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum TokenError
{
	ACCESS_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCESS TOKEN NOT FOUND (액세스 토큰이 없음)"),
	REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "REFRESH TOKEN NOT FOUND (리프레쉬 토큰이 없음)");
	
	TokenError(HttpStatus status, String message)
	{
		this.status = status;
		this.message = message;
	}
	
	private final HttpStatus status;
	private String message;
}
