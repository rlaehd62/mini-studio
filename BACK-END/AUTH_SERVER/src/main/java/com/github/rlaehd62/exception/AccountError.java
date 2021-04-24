package com.github.rlaehd62.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum AccountError
{
	ACCOUNT_EXIST(HttpStatus.BAD_REQUEST, "ACCOUNT ALREADY EXISTS (이미 존재하는 계정은 만들 수 없음)"),
	ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "NO ACCOUNT FOUND (계정을 찾을 수 없음)"),
	ACCOUNT_DENY(HttpStatus.FORBIDDEN, "ACCESS DENY (계정에 대한 접근이 거부됨)"),
	ACCOUNT_NO_PERMISSION(HttpStatus.UNAUTHORIZED, "YOU HAVE NO PERMISSION ON THIS ACCOUNT (계정에 대한 권한이 없음)");
	
	AccountError(HttpStatus status, String message)
	{
		this.status = status;
		this.message = message;
	}
	
	private final HttpStatus status;
	private String message;
}
