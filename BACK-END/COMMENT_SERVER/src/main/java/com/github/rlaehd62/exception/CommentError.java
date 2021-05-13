package com.github.rlaehd62.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum CommentError
{
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT NOT FOUND (댓글을 찾지 못함)"),
	COMMENT_NOT_MINE(HttpStatus.BAD_REQUEST, "COMMENT ACCESS DENIED (댓글에 대한 권한 없음)");
	
	CommentError(HttpStatus status, String message)
	{
		this.status = status;
		this.message = message;
	}
	
	private final HttpStatus status;
	private String message;
}
