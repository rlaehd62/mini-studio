package com.github.rlaehd62.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum BoardError
{
	BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "BOARD NOT FOUND (게시물을 찾지 못함)"),
	BOARD_NOT_MINE(HttpStatus.BAD_REQUEST, "BOARD ACCESS DENIED (게시물에 대한 권한 없음)");
	
	BoardError(HttpStatus status, String message)
	{
		this.status = status;
		this.message = message;
	}
	
	private final HttpStatus status;
	private String message;
}
